#!/bin/bash

IFS=$'\n'

#是否是调试状态
DEBUG=0

#版本名称
VERSION_NAME=$1

#shell执行目录
PWD=$(pwd)

#工程目录
PROJECT_PATH=$(pwd)

#获取gradle项目根目录名字
PROJECT_NAME=${PROJECT_PATH##*/}

#文件操作目录（包括获取资源文件，生成工程目录等）
OPERATE_PATH="${HOME}/app"

#目标目录名字
TARGETS_DIR_NAME="flavors"

#描述文件名
MK_FILE_NAME="makefile"

#参数文件名
properties="parameter.properties"

#临时文件目录
TEMP_PATH="${HOME}/.${TARGETS_DIR_NAME}-work/${PROJECT_NAME}"

#目录apk输出路径
TARGET_APK_PATH="${OPERATE_PATH}/apk/${VERSION_NAME}"

#工程快照名字
SNAPSHOT_NAME=".flavor-snapshot"

#工程快照
SNAPSHOT_PATH="${OPERATE_PATH}/${SNAPSHOT_NAME}"

#target保存路径
TARGETS_PATH="${SNAPSHOT_PATH}/${TARGETS_DIR_NAME}"

config_file=""

if [ -z $1 ]; then
    echo "使用 -h|--help 查看帮助信息"
    exit 1
fi
case "$1" in -h|--help|?)
    echo "<版本名称，如：3.0.1>"
    echo "[指定打包站点 code, 可多个]"
    exit 0
    ;;
    date)
    echo `date +%Y%m`
    exit 2
esac

echo "本次打包版本号为：$1"

#指定打包目录
arr_code=($@)
#去除第一个参数
unset arr_code[0]
#指定个数
arr_code_len=${#arr_code[@]}
echo "指定打包站点个数：${arr_code_len} 个"
#for i in ${arr_code[@]}; do
#    echo "$i"
#done

#===util function start===
#打印调试日志
function dlog() {
    if [ ${DEBUG} != 0 ];then
         echo $1
    fi
}

#打印日志
function log() {
    echo $1
}

#打印错误信息
function elog() {
    echo "--warn, ${1}"
}

#是否是gradle根项目
#args  : ${1}: 被检测目录
#return: 1: yes 0: no
function is_root_gradle_project() {
    dlog "is_root_gradle_project |${1}|"
    if [ -f "${1}/build.gradle" ] && [ -f "${1}/settings.gradle" ];then
        return 1
    fi
    return 0
}

#是否是gradle项目
#args  : ${1}: 被检测目录
#return: 1: yes 0: no
function is_gradle_project() {
   if [ -f "${1}/build.gradle" ];then
        return 1
   fi
   return 0
}

#是否是app的gradle项目
#args  : ${1}: 被检测目录
#return: 1: yes 0: no
function is_app_gradle_project() {
   if [ -f "${1}/build.gradle" ];
   then
        cat "${1}/build.gradle" | grep 'com.android.application' > /dev/null 2>&1
        if [ $? == 0 ];then
           return 1
        fi
   fi
   return 0
}
#=util function end=

#===plugin function start ===

#替换多个文件中的内容 ${1}: 目标module ${2}: 从这个路径下开始搜索 ${3}: 被替换的内容 ${4}: 目标内容
function match_all() {
    dlog "match_all target: ${1},search_root: ${2},src_str: ${3},dest_str: ${4}"
    search_path="${SNAPSHOT_PATH}/${1}/${2}"
    dlog "search_path: ${search_path}"

    find ${search_path} | while read line
    do
        #忽略掉文件夹
        if [ -f ${line} ];then
            cat $line | grep $3
            if [ $? == 0 ];then
                  dlog "match_all : ${line}"
            fi
            sed -i.zztmp "s/${3}/${4}/g" ${line}
            rm "${line}.zztmp"  > /dev/null 2>&1
        fi
    done
}

#复制文件 ${1}: 目标module  ${2}: 应用的名字
function app_name() {
    dlog "app_name |${1},${2}"
    log "${1} rename app_name to ${2}"

    file_path="${SNAPSHOT_PATH}/${1}/src/main/AndroidManifest.xml"
    manifest_doc=$(cat ${file_path} | tr -d '\n')
    application_node=$(echo ${manifest_doc} | grep -o -E '(<application[^>]{1,}>)')
    application_node_target=$(echo ${application_node} | sed -E "s/(android:label=\"[^\"]{1,}\")/android:label=\"${2}\"/g")
    manifest_doc=${manifest_doc/${application_node}/${application_node_target}}
    echo ${manifest_doc} > ${file_path}
}

#复制文件 ${1}: 目标module  ${2}: 源文件相对路径 ${3}: 目标文件相对路径
function copy_file() {
    dlog "copy_file |${1},${2},${3}|"

    src_file_path="${TARGETS_PATH}/${1}/${2}"
    target_file_path="${SNAPSHOT_PATH}/${1}/${3}"

    dlog "copy_file src_file_path: ${src_file_path}"
    if [ ! -f ${src_file_path} ];then
        elog "file not found!!  ${src_file_path}"
        exit 1
    fi
    dlog "copy_file target_file_path: ${target_file_path}"
    cp ${src_file_path} ${target_file_path}
}

#清空皮肤
function clear_skin() {
    dlog "clear_skin |${1},${2}|"

    src_file_path="${TARGETS_PATH}/${1}/${2}"
    target_file_path="${SNAPSHOT_PATH}/${1}/${2}"

    rm -rf /${target_file_path}/*
}

#替换build文件中的内容 ${1}: 目标module  ${2}: 目标文件相对路径 ${3}: 被替换的内容 ${4}: 目标内容
function replace_build() {
    target=${1}
    target_file=${2}

    src_str=${3}
    dest_str=${4}
    echo "replace_build target: ${1},target_file: ${target_file},src_str: ${src_str},dest_str: ${dest_str}"

    file_path="${SNAPSHOT_PATH}/${target}/${target_file}"
    src=${src_str//_space/ }
    dest=${dest_str//_space/ }

    sed -i.zztmp "s|\(${src}\).*$|\1${dest}|g"  ${file_path}
    rm "${file_path}.zztmp" > /dev/null 2>&1
}

#替换单体文件中的内容 ${1}: 目标module  ${2}: 目标文件相对路径 ${3}: 被替换的内容 ${4}: 目标内容
function match_file() {
    target=${1}
    target_file=${2}

    src_str=${3}
    dest_str=${4}
    echo "match_file target: ${1},target_file: ${target_file},src_str: ${src_str},dest_str: ${dest_str}"

    file_path="${SNAPSHOT_PATH}/${target}/${target_file}"
    sed -i.zztmp "s/${src_str}/${dest_str}/g" ${file_path}
    rm "${file_path}.zztmp" > /dev/null 2>&1
}

#替换属性内容 ${1}: 目标module  ${2}: 目标文件相对路径 ${3}: 被替换的内容 ${4}: 目标内容
function match_properties() {
    target=${1}
    target_file=${2}

    src_str=${3}
    dest_str=${4}
    dlog "match_file target: ${1},target_file: ${target_file},src_str: ${src_str},dest_str: ${dest_str}"

    file_path="${SNAPSHOT_PATH}/${target}/${target_file}"
    dest=${dest_str//_space/ }
    sed -i.zztmp "s#^${src_str}=.*#${src_str}=${dest}#g"  ${file_path}
    rm "${file_path}.zztmp" > /dev/null 2>&1
}

#替换项目包名
function package() {
    target=${1}
    src_project=${2}
    package_name=${3}

    #目标工程路径
    target_dir="${SNAPSHOT_PATH}/${target}"

    cat "${target_dir}/build.gradle" | grep applicationId | awk '{print $2}' > /dev/null 2>&1
    if [ $? != 0 ];then
        elog "resolve old package error, when get old package name. check your build.gradle applicationId"
        exit 1
    fi

    old_package_name=$(cat "${target_dir}/build.gradle" | grep applicationId | awk '{print $2}')
    old_package_name=${old_package_name#*\"}
    old_package_name=${old_package_name%\"*}

    log "rename $target package ${old_package_name} to $package_name"

    match_file ${target} "build.gradle" ${old_package_name} ${package_name}
}


function change_parameter() {
   parameter=${1}
   makefile=${2}
    dlog "change_parameter: ${1}    ${2}"
   cat ${parameter} | while read line
   do
        key=${line%=*}
        val=${line#*=}
        sed -i.zztmp "s/${key}/${val}/g" ${makefile} #> /dev/null 2>&1
        rm "${makefile}.zztmp" > /dev/null 2>&1
        echo $key $val
   done
}

#===plugin function end ===
set -x
#预处理makefile
function pretreatment_makefile() {
    makefile=${1}
    #替换环境变量
    env_var_key_array=("\${src}" "\${res}" "\${assets}")
    env_var_value_array=("src\/main\/java" "src\/main\/res" "src\/main\/assets" )
    dlog "pretreatment_makefile: ${1}"

    index=0
    for key in ${env_var_key_array[@]}
    do
        val=${env_var_value_array[index]}
#        dlog "key: ${key},val: ${val}"

        sed -i.zztmp "s/${key}/${val}/g" ${makefile} #> /dev/null 2>&1
        rm "${makefile}.zztmp" > /dev/null 2>&1
        index=$((index + 1))
    done
}

#生成target  ${1}: 目标名字  ${2}: 源项目
function generate_target() {
    #创建快照下flavors目录
    if [ ! -d ${SNAPSHOT_PATH}/${TARGETS_DIR_NAME} ];then
        mkdir -p ${SNAPSHOT_PATH}/${TARGETS_DIR_NAME}
    fi

    dlog "generate_target |${1},${2}|"
    target=${1}
    src_project=${2}

    #复制一个以target命名的新项目，以src_project为蓝本
    dlog "cp  ${SNAPSHOT_PATH}/${src_project} to ${SNAPSHOT_PATH}/${target}"
    cp -r "${SNAPSHOT_PATH}/${src_project}" "${SNAPSHOT_PATH}/${target}"
    #把新的工程配置添加到settings.gradle
    echo " " >> "${SNAPSHOT_PATH}/settings.gradle"
    echo "include ':${target}'" >> "${SNAPSHOT_PATH}/settings.gradle"

    cp -rf "./${TARGETS_DIR_NAME}/${target}" "${SNAPSHOT_PATH}/${TARGETS_DIR_NAME}/${target}"

    #判断makefile文件是否存在
    config_file="${TARGETS_PATH}/${target}/${MK_FILE_NAME}"
    if [ ! -f $config_file ];then
        elog "makefile not found 2: ${config_file}"
        exit 1;
    fi

    #预处理makefile
    pretreatment_makefile ${config_file}
    change_parameter "${TARGETS_PATH}/${target}/${properties}" ${config_file}

    #解析并执行描述文件
    elog "${config_file}     解析并执行描述文件"
    cat $config_file | while read line
    do
        line=$(echo $line)
        if [ "$line" != "" ] && [ "${line:0:1}" != '#' ];then
            action=$(echo $line | awk '{print $1}')
            if [ "$action" != "package" ];then
                #调用插件对应的函数并传参
                dlog "《《《 ${line/$action/$target}"
                "$action" ${target} $(echo $line | awk '{print $2}') $(echo $line | awk '{print $3}') $(echo $line | awk '{print $4}') $(echo $line | awk '{print $5}')
            else
                #调用插件对应的函数并传参
                dlog "《《《 ${line/$action/$target}"
                "$action" ${target} ${src_project} $(echo $line | awk '{print $2}') $(echo $line | awk '{print $3}') $(echo $line | awk '{print $4}') $(echo $line | awk '{print $5}')
            fi
        fi
    done
}

#初始化上下文
function init_context() {
    #删除上次的快照
    rm -rf ${SNAPSHOT_PATH}
    log "Generating project snapshot .... "
    #清理项目临时文件
    #gradle clean > /dev/null 2>&1

    #如果临时文件路径不存在就创建
    if [ ! -d ${TEMP_PATH} ];then
        mkdir -p ${TEMP_PATH}
    fi

    #生成项目快照，保存在工作目录
    cp -r ${PROJECT_PATH} ${TEMP_PATH}
    mv ${TEMP_PATH}/${PROJECT_NAME} ${SNAPSHOT_PATH}

    #把工作目录加入到.gitignore
    if [ -d "${PROJECT_PATH}/.git" ];then
        if [ ! -f "${PROJECT_PATH}/.gitignore" ];then
            echo ${SNAPSHOT_NAME} > "${PROJECT_PATH}/.gitignore"
        else
             cat ${PROJECT_PATH}/.gitignore | grep "${SNAPSHOT_NAME}" > /dev/null 2>&1
             if [ $? == 1 ];then
                echo " " >> "${PROJECT_PATH}/.gitignore"
                echo ${SNAPSHOT_NAME} >> "${PROJECT_PATH}/.gitignore"
             fi
        fi
    fi
}

function generate_targets() {
    set +x  #停用调试

    #判断脚本执行的路径是否是gradle工程的根路径
    #echo ${PROJECT_PATH}
    is_root_gradle_project ${PROJECT_PATH}

    if [ $? == 0 ];then
        elog "execution path is not root gradle project(build.gradle or settings.gradle not found)"
        exit 1
    fi

    #需要生成的目标数组
    TARGET_ARRAY=()
    index=1;

    if [ $arr_code_len -gt 0 ]; then
        echo "开始打包指定站点..."
        code_preifx="app_"
#        TARGET_ARRAY=(${arr_code[*]})  #直接赋值
        for i in ${arr_code[@]}; do
            TARGET_ARRAY[index]=$code_preifx$i
            let index=index+1
        done
    else
        echo "开始打包全部站点..."
        #扫描需要生成的target
        for file in $(ls "${OPERATE_PATH}/${TARGETS_DIR_NAME}")
          do
           if [ -d "${OPERATE_PATH}/${TARGETS_DIR_NAME}/$file" ]  && [ $file != 'out' ] && [ $file != 'ignore' ] ;then
                cat "${OPERATE_PATH}/${TARGETS_DIR_NAME}/.flavorignore"  2> /dev/null  | grep "${file}" > /dev/null
                skip=$?

                if [ ! -f ${zzignore_file} ] || [ ${skip} == 1 ];then
                    #判断是否是有效的target名字(以存在的app项目的名字加上下划线开头)
                    is_app_gradle_project ${file%_*}

                    if [[ $? == 1 ]];then
                        #检查makefile是否存在
                        if [ ! -f "${OPERATE_PATH}/${TARGETS_DIR_NAME}/${file}/${MK_FILE_NAME}" ];then
                             elog "makefile not found 1: ${OPERATE_PATH}/${TARGETS_DIR_NAME}/${file}/${MK_FILE_NAME}"
                             exit 1;
                        fi

                        TARGET_ARRAY[$index]=$file
                        index=$((index + 1))
                    else
                        elog "invalid target: '$file', '${OPERATE_PATH}/${file%_*}' is not a valid gradle android application project"
                        exit 1
                    fi
                fi
           fi
        done
     fi

    #初始化环境
    init_context

    #生成各个目标的源代码
    for target in ${TARGET_ARRAY[@]}; do
        generate_target ${target} ${target%_*}
    done

    rm -rf ${TARGET_APK_PATH}

    report_file="${TEMP_PATH}/report_file.txt"
    rm -rf ${report_file};touch ${report_file}

    log '====> GENERATING APK START...'
    #打包apk
    for target in ${TARGET_ARRAY[@]}
    do
        cd ${SNAPSHOT_PATH}/${target}
        gradle clean assembleRelease
        if [ $? == 0 ];then
            echo "BUILD SUCCESS  TARGET: ${target}" >> ${report_file}
            if [ ! -d ${TARGET_APK_PATH} ];then
                mkdir -p ${TARGET_APK_PATH}
            fi

            dlog "Copying apk with gradle ..."
            ls ${SNAPSHOT_PATH}/${target}/build/outputs/apk | while read line
            do
                # 拷贝生成的apk到指定目录
                cp "${SNAPSHOT_PATH}/${target}/build/outputs/apk/${line}/release/app.apk" "${TARGET_APK_PATH}/${target}_${VERSION_NAME}.apk"
            done

            rm -r "${SNAPSHOT_PATH}/${target}"
            rm -r "${SNAPSHOT_PATH}/${TARGETS_DIR_NAME}"
        else
            echo "BUILD FAIL     TARGET: ${target}" >> ${report_file}
        fi
    done

    #输出报告
    echo ''
    log '==========> BUILD REPORT <=========='
    if [ -d ${TARGET_APK_PATH} ];then
        log 'ALL GENERATED APK IN '${TARGET_APK_PATH}
        ls ${TARGET_APK_PATH} | while read line
        do
             log "   ${line}"
        done
        echo ''
    fi
    cat ${report_file}
}

#根据flavors目录配置的信息，生成目标apk
generate_targets

