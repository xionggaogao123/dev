var urls=[
	"http://www.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",
	"http://yun.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",
	"http://www.fulaan.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",
	"http://midong.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction"
];


var clientSuccess=0;
var thisIndex=-1;
var location1="",location2="",location3="";

function loginK6ktSso()
{
	var ui=getCookie("ui");
	var cui=getCookie("c_ui");

	var cValue=(cui?cui:ui);

	if(cValue)
	{
		var url=location.href;

		if(url.indexOf("yun.k6kt.com")>=0)
		{
			thisIndex=1;
			location1=urls[0];
			location2=urls[2];
		}
		else if(url.indexOf("www.k6kt.com")>=0)
		{
			thisIndex=0;
			location1=urls[1];
			location2=urls[2];
			location3=urls[3];
		}
		else if(url.indexOf("www.fulaan.com")>=0)
		{
			thisIndex=2;
			location1=urls[0];
			location2=urls[1];
			location3=urls[3];
		}
		else if(url.indexOf("midong.k6kt.com")>=0)
		{
			thisIndex=1;
			location1=urls[0];
			location2=urls[2];
		}

		if(thisIndex==-1)
		{
			return;
		}


		location1=location1+"&cValue="+cValue;
		location2=location2+"&cValue="+cValue;


		var iframe = document.createElement("iframe");
		iframe.src = location1;
		iframe.style.display="none";

		if (iframe.attachEvent){
			iframe.attachEvent("onload", function(){
				gotoPage();
			});
		} else {
			iframe.onload = function(){
				gotoPage();
			};
		}
		document.body.appendChild(iframe);




		var iframe2 = document.createElement("iframe");
		iframe2.src = location2;
		iframe2.style.display="none";

		if (iframe2.attachEvent){
			iframe2.attachEvent("onload", function(){
				gotoPage();
			});
		} else {
			iframe2.onload = function(){
				gotoPage();
			};
		}
		document.body.appendChild(iframe2);

		if(location3!=""){
			location3=location3+"&cValue="+cValue;
			var iframe3 = document.createElement("iframe");
			iframe3.src = location3;
			iframe3.style.display="none";

			if (iframe3.attachEvent){
				iframe3.attachEvent("onload", function(){
					gotoPage();
				});
			} else {
				iframe3.onload = function(){
					gotoPage();
				};
			}
			document.body.appendChild(iframe3);
		}
	}
}


function gotoPage()
{
	clientSuccess=clientSuccess+1;
	if(clientSuccess==2)
	{
		if(thisIndex==0)
		{
			location.href="/user/homepage.do";
		}
		if(thisIndex==1)
		{
			location.href="/user/gotomainpage.do";
		}
	}
}





function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	}
	return "";
}


