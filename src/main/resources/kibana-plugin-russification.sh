#!/bin/bash

if [ "$1" = "--help" ]
then
	echo "kibana-plugin-russification.sh <path/to/kibana> </path/to build tool>"
	exit
fi


for dir in `ls $1/src/legacy/core_plugins`
do
	
	[ ! -d "$1/src/legacy/core_plugins/$dir/translations" ] && mkdir $1/src/legacy/core_plugins/$dir/translations
	node scripts/i18n_extract --path $1/src/legacy/core_plugins/$dir --output-dir $1/src/legacy/core_plugins/$dir/translations
	[ -f $1/src/legacy/core_plugins/$dir/translations/en.json ] && time java -Dfile.encoding=UTF-8 -jar $2/gwt-selenium.jar -webdriver-home webdriver.chrome.driver -webdriver-location $2/webdriver/chromedriver.exe -f EN -t RU $1/src/legacy/core_plugins/$dir/translations/en.json
	#echo "mv $1/src/legacy/core_plugins/$dir/translations/en.json.en.ru.out $1/src/legacy/core_plugins/$dir/translations/ru.json"
done
