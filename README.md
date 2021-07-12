## REST API 
A REST API  built using `Play` framework in `scala` that fetches data from local `Mongodb` database.
<hr>

To Obtain APIs used in a project:
1. Open Windows PowerShell
2. Navigate to the project directory
3. Use the following 2 commands:
	```
	$UrlRegex ="https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)";
	Get-ChildItem -Recurse -File -Path $DocsRootPath |  Select-String -pattern "$UrlRegex"  -AllMatches | % { $_.Matches } | % { $_.Value };
	```
