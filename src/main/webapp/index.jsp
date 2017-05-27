<html>
<head>
    <script src="pano/js/krpano.js"></script>
    <script src="pano/js/jquery-3.2.1.min.js"></script>
</head>
<body>
<div id="pano" style="width:100%;height:100%;"></div>
<noscript>&lt;table style="width:100%;height:100%;"&gt;&lt;tr style="vertical-align:middle;"&gt;&lt;td&gt;&lt;div
    style="text-align:center;"&gt;ERROR:&lt;br/&gt;&lt;br/&gt;Javascript not activated&lt;br/&gt;&lt;br/&gt;&lt;/div&gt;&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;
</noscript>
<script>

    embedpano({
        swf: "pano/krpano.swf",
        xml: "pano/pano/fe628jaOwcv/pano.xml",
        target: "pano",
        html5: "auto",
        wmode: 'opaque-flash'
    });
</script>

</body>
</html>

