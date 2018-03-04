(function(){var lastKeyDownWhich=-1;var lastPosition=-1;if(!Array.prototype.indexOf){Array.prototype.indexOf=function(el){for(var i=0;i<this.length;i++){if(this[i]===el){return i}}return-1}}var _setSelectionRange=function(input,selectionStart,selectionEnd){if(input.setSelectionRange){input.setSelectionRange(selectionStart,selectionEnd)}else if(input.createTextRange){var range=input.createTextRange();range.collapse(true);range.moveEnd('character',selectionEnd);range.moveStart('character',selectionStart);range.select()}};var _setCaretToPos=function(input,pos){_setSelectionRange(input,pos,pos)};var preventDefault=function(e){if(e.preventDefault){e.preventDefault()}else{e.returnValue=false}};var getNormalCaret=function(el){var start=el.selectionStart;var end=el.selectionEnd;if(start===end){return start}return[start,end]};var getIECaret=function(el){var start=0;var range=el.createTextRange();var range2=document.selection.createRange().duplicate();var range2Bookmark=range2.getBookmark();range.moveToBookmark(range2Bookmark);var end=0;while(range.moveStart('character',-1)!==0){start++}while(range.moveStart('character',1)!==0){end++}return start};var keyDownEventListener=function(e,curConfig,curEnableKeys){var which=e.charCode?e.charCode:e.keyCode;var target=e.target?e.target:e.srcElement;lastKeyDownWhich=which;var position=target.selectionStart!==undefined?getNormalCaret(target):getIECaret(target);lastPosition=position;if(e.shiftKey){preventDefault(e);return}if(e.ctrlKey){if(which!==67&&which!==86&&which!==88){preventDefault(e);return}}if(curEnableKeys.indexOf(which)===-1&&which!==13&&!e.ctrlKey){preventDefault(e);return}if((which===190||which===110)&&target.value.indexOf(".")!==-1){preventDefault(e);return}var selectedText=window.getSelection().toString();if((which===109||which===189||which===173)&&(target.value.indexOf("-")!==-1)&&!selectedText){preventDefault(e);return}var digits=target.value.split(".");if(digits.length===2&&digits[1].length>=curConfig.decimalSize&&((which>=48&&which<=57)||(which>=96&&which<=105))){if(typeof position==="number"&&position>digits[0].length){preventDefault(e);return}}if(digits.length>0&&digits[0].length>=curConfig.intSize&&((which>=48&&which<=57)||(which>=96&&which<=105))){if(typeof position==="number"&&position<=curConfig.intSize){preventDefault(e)}}};var keyUpEventListener=function(e,curConfig,hasEqual){var which=e.charCode?e.charCode:e.keyCode;var target=e.target?e.target:e.srcElement;var digits=target.value.split(".");if(/[^\x00-\xff]/g.test(target.value)){target.value=target.value.replace(/[^\x00-\xff]/g,"")}else if(target.value.match(/~|@|#|%|&|\*|\+|\{|\}|\|/g)){var matchChar=target.value.match(/~|@|#|%|&|\*|\+|\{|\}|\|/g);target.value=target.value.replace(new RegExp("\\"+matchChar[0],"g"),"")}else if(!hasEqual&&target.value.match(/=/g)){target.value=target.value.replace(new RegExp("\\"+target.value.match(/=/g)[0],"g"),"")}else if(target.value.match(/-/g)&&!curConfig.negative){target.value=target.value.replace(/-/g,"")}else if(/[_]/g.test(target.value)){target.value=target.value.replace(/[_]/g,"")}else if(/^.+-.*$/.test(target.value)){var tmp=target.value.substring(1);target.value=target.value.charAt(0)+tmp.replace(/[-]/g,"")}else if(digits.length>0&&digits[0].length>curConfig.intSize){target.value=target.value.substring(digits[0].length-curConfig.intSize)}else if(lastKeyDownWhich===229&&((which>=96&&which<=105)||(which>=48&&which<=57)||which===190||which===189||which===109||which===110)){if(which>=48&&which<=57){target.value=target.value.substring(0,lastPosition)+""+(which-48)+""+target.value.substring(lastPosition);_setCaretToPos(target,lastPosition+1)}else if(which>=96&&which<=105){target.value=target.value.substring(0,lastPosition)+""+(which-96)+""+target.value.substring(lastPosition);_setCaretToPos(target,lastPosition+1)}else if(which===190||which===110){target.value=target.value.substring(0,lastPosition)+"."+target.value.substring(lastPosition);_setCaretToPos(target,lastPosition+1)}else if(which===189||which===109){}}if(e.ctrlKey){target.value=target.value.replace(/,/g,"");if(isNaN(target.value)){target.value=""}else{var newIntStr="";var newDecimalStr="";var oldIntStr=target.value.split(".")[0];newIntStr=oldIntStr;if(oldIntStr.length>curConfig.intSize){newIntStr=oldIntStr.substring(0,curConfig.intSize)}if(target.value.split(".").length===2){var oldDecimalStr=target.value.split(".")[1];newDecimalStr=oldDecimalStr;if(!curConfig.decimal){newDecimalStr=""}else if(curConfig.decimal&&oldDecimalStr.length>curConfig.decimalSize){newDecimalStr=oldDecimalStr.substring(0,curConfig.decimalSize)}}target.value=newIntStr+(newDecimalStr?".":"")+newDecimalStr}}};var dropEventListener=function(e){preventDefault(e)};var eventMap=[];var handlePerDom=function(dom,curConfig,curEnableKeys,hasEqual){var keyDownEvent=(function(curConfig,curEnableKeys){return function(e){keyDownEventListener(e,curConfig,curEnableKeys)}})(curConfig,curEnableKeys);var keyUpEvent=(function(curConfig,curEnableKeys,hasEqual){return function(e){keyUpEventListener(e,curConfig,hasEqual)}})(curConfig,curEnableKeys,hasEqual);if(dom.addEventListener){dom.addEventListener("drop",dropEventListener,false);dom.addEventListener("keydown",keyDownEvent,false);dom.addEventListener("keyup",keyUpEvent,false)}else{dom.attachEvent("ondrop",dropEventListener);dom.attachEvent("onkeydown",keyDownEvent);dom.attachEvent("onkeyup",keyUpEvent)}var domEvent={dom:dom,keyDownEvent:keyDownEvent,keyUpEvent:keyUpEvent};eventMap.push(domEvent)};var initConfig=function(config){var defaultConfig={negative:true,decimal:true,intSize:12,decimalSize:4};if(config){for(var attr in config){if(config.hasOwnProperty(attr)){defaultConfig[attr]=config[attr]}}}return defaultConfig};var initEnableKeys=function(config){var enableKeys=[48,49,50,51,52,53,54,55,56,57,8,46,37,39,35,36,96,97,98,99,100,101,102,103,104,105];if(config.negative){enableKeys.push(189);enableKeys.push(109);enableKeys.push(173)}if(config.decimal){enableKeys.push(190);enableKeys.push(110)}return enableKeys};var clearPerDom=function(dom){for(var i=0;i<eventMap.length;i++){if(dom===eventMap[i].dom){if(dom.removeEventListener){dom.removeEventListener("drop",dropEventListener,false);dom.removeEventListener("keydown",eventMap[i].keyDownEvent,false);dom.removeEventListener("keyup",eventMap[i].keyUpEvent,false)}else{dom.detachEvent("ondrop",dropEventListener);dom.detachEvent("onkeydown",eventMap[i].keyDownEvent);dom.detachEvent("onkeyup",eventMap[i].keyUpEvent)}}}};var inputNumber={init:function(domObj,config,hasEqual){var curConfig=initConfig(config);var curEnableKeys=initEnableKeys(curConfig);if(jQuery&&domObj instanceof jQuery){for(var i=0;i<domObj.length;i++){domObj.eq(i).bind("contextmenu",function(e){e.preventDefault()});handlePerDom(domObj.eq(i)[0],curConfig,curEnableKeys,hasEqual)}}else{if(domObj.tagName===undefined){for(var i=0;i<domObj.length;i++){handlePerDom(domObj[i],curConfig,curEnableKeys,hasEqual)}}else{handlePerDom(domObj,curConfig,curEnableKeys,hasEqual)}}},clear:function(domObj){if(jQuery&&domObj instanceof jQuery){for(var i=0;i<domObj.length;i++){clearPerDom(domObj.eq(i)[0])}}else{if(domObj.tagName===undefined){for(var i=0;i<domObj.length;i++){clearPerDom(domObj[i])}}else{clearPerDom(domObj)}}}};if(typeof define==="function"&&define.amd){define(function(){return inputNumber})}else{window.inputNumber=inputNumber}})();
