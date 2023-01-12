<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href=<%=basePath%>>
<%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    引入插件--%>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
    <title>演示echarts报表插件</title>
    <script type="text/javascript">
        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '交易统计图表',
                    subtext: '交易表中各个阶段的数量'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b} : {c}'
                },
                toolbox: {
                    feature: {
                        dataView: { readOnly: false },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                series: [
                    {
                        name: '数据量',
                        type: 'funnel',
                        left: '10%',
                        width: '80%',
                        label: {
                            formatter: '{b}'
                        },
                        labelLine: {
                            show: true
                        },
                        itemStyle: {
                            opacity: 0.7
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}: {c}'
                            }
                        },
                        data: [
                            { value: 60, name: 'Visit' },
                            { value: 40, name: 'Inquiry' },
                            { value: 20, name: 'Order' },
                            { value: 80, name: 'Click' },
                            { value: 100, name: 'Show' }
                        ]
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        })
    </script>
</head>
<body>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</body>
</html>
