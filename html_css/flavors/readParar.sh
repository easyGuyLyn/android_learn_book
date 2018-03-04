#！/bin/bash
echo "已经导入站点配置参数!"
#cat ./*/parameter.properties > para.text
file="parameter.properties"
para_file="para.text"
echo "创建文件para.text"
echo > para.text
for dir in ./*/
do 
echo $dir
file_path=${dir}$file
echo $file_path
echo "*****************************************************" >> $para_file
cat $file_path >> $para_file
echo "*****************************************************" >> $para_file
done

