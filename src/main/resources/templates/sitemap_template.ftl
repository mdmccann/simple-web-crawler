<html>
    <head>
        <title>SiteMap for ${BASE_URL}</title>

        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
            }
        </style>
    </head>
    <body>
        <table>
            <tr>
                <th>Unique URLs</th>
                <th>Containing Links</th>
            </tr>
            <#list PAGE_LINK_MAP as key, value>
                <tr>
                    <td><a href="${key}">${key}</a></td>
                    <td>
                        <ul>
                            <#list value as containingLink>
                                <li><a href="${containingLink}">${containingLink}</a></li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </#list>
            <tr>
                <th colspan="2">Broken Links</th>
            </tr>
            <tr>
                <td colspan="2">
                    <ul>
                        <#list BROKEN_LINKS as brokenLink>
                            <li><a href="${brokenLink}">${brokenLink}</a></li>
                        </#list>
                    </ul>
                </td>
            </tr>
        </table>
    </body>
</html>