#注意: 参数中不要出现空格,如果某个参数需要使用空格使用_space代替

#支持的内置常量:
#   ${src}      代替 src/main/java
#   ${res}      代替 src/main/res
#   ${assets}   代替 src/main/assets

#复制文件(如果对应的文件存在就覆盖掉)

clear_skin ${res}/drawable-hdpi/
clear_skin ${res}/mipmap-xhdpi/
clear_skin ${res}/mipmap-xxhdpi/
clear_skin ${res}/mipmap-xxxhdpi/


copy_file ${app_logo}.png ${res}/drawable-xhdpi/${app_logo}.png
copy_file mipmap-xhdpi/${app_icon}.png ${res}/mipmap-xhdpi/${app_icon}.png
copy_file mipmap-xxhdpi/${app_icon}.png ${res}/mipmap-xxhdpi/${app_icon}.png
copy_file mipmap-xxxhdpi/${app_icon}.png ${res}/mipmap-xxxhdpi/${app_icon}.png

copy_file mipmap-xxhdpi/ic_launcher_round.png ${res}/mipmap-xxhdpi/ic_launcher_round.png
copy_file mipmap-xxxhdpi/ic_launcher_round.png ${res}/mipmap-xxxhdpi/ic_launcher_round.png

#替换build.gradle的app_name
replace_build build.gradle resValue_space\"string\",_space\"app_name\",_space \"${app_name}\"

#替换build.gradle的app_code
replace_build build.gradle resValue_space\"string\",_space\"app_code\",_space \"${app_code}\"

#替换build.gradle的app_sid
replace_build build.gradle resValue_space\"string\",_space\"app_sid\",_space \"${app_sid}\"

#替换build.gradle的site_type
replace_build build.gradle resValue_space\"string\",_space\"site_type\",_space \"${site_type}\"

#替换build.gradle的app_logo
replace_build build.gradle resValue_space\"string\",_space\"app_logo\",_space \"${app_logo}\"

#替换build.gradle的applicationId
replace_build build.gradle applicationId_space \"${applicationId}\"

#替换build.gradle的amanifestPlaceholders
replace_build build.gradle manifestPlaceholders_space=_space [app_icon:\"@mipmap/${app_icon}\"]

#替换build.gradle的theme
replace_build build.gradle resValue_space\"string\",_space\"theme\",_space \"${theme}\"

#清空皮肤文件夹的所有皮肤
clear_skin ${assets}/skin/
#复制进去所需皮肤（无皮肤注释掉）
copy_file ${theme} ${assets}/skin/
