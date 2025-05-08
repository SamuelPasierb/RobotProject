<html>
    <head>
        <link rel="stylesheet" href="styles.css">
        <meta charset="UTF-8"> 
        <title>Lego Controller</title>
    </head>
    <body>

        <div id="left">
            <h1>Robot State</h1>
            <div class="objects">
                <div class="object">
                    <h3 class="object-title">Speed</h3>
                    <div id="speedometer" class="pie" data-pie='{ 
                        "size": 350,
                        "number": false,
                        "cut": 30,
                        "percent": 0,
                        "rotation": 144,
                        "colorSlice": "#F50057",
                        "colorCircle": "#f1f1f1"}'>
                        <h3 id="speed">0 m/s</h3>
                    </div>
                    <h4 id="average-speed">Average speed: not enought data</h4>
                </div>
                <div class="object">
                    <iframe src="./rest/lego/currentdata" id="data-iframe"></iframe>
                </div>
            </div>
        </div>

        <div id="right">
            <h1>Robot Controller</h1>

            <ul>
                <li>
                    <h3 class="speed-setter">0</h3>
                    <form action="./rest/lego/changespeed" method="POST" id="speed-form" class="speed-setter">
                        <input type="range" min="0" max="500" value="0" class="slider" id="speedRange" name="speed">
                    </form>
                    <h3 class="speed-setter">500</h3>
                </li>
                <li>
                    <h3>Set Speed Manually</h3>
                    <form action="./rest/lego/setspeed" method="POST">
                        <input id="set-speed" type="text" name="setspeed" value="">
                        <input type="submit" name="ok" value="Send" id="speed-setter">
                    </form>
                </li>
                <li>
                    <h3>Line Follower Switch</h3>
                    <form action="./rest/lego/line" method="POST" id="line-form">
                        <input type="checkbox" name="linefollower" id="line-switch" onclick="document.getElementById('line-form').submit()" value="true">
                    </form>
                </li>
                <li>
                    <h3>Obstacle avoidance</h3>
                    <form action="./rest/lego/avoidance" method="POST" id="avoidance-form">
                        <input type="radio" name="avoidance-type" value="STOP" onclick="avoidanceForm()" checked>
                        <label for="radio1">Just stop</label>
                        <input type="radio" name="avoidance-type" value="TURN_AROUND" onclick="avoidanceForm()">
                        <label for="radio2">Turn around</label>
                        <input type="radio" name="avoidance-type" value="GO_AROUND" onclick="avoidanceForm()">
                        <label for="radio3">Go around</label>
                    </form>
                </li>
                <li>
                    <form action="./rest/lego/turn/left" method="POST"><input type="submit" name="turnleft" value="TURN LEFT" onclick="turndegrees(-1)"></form>
                    <h4 id="turndegrees">0&#176;</h4>
                    <form action="./rest/lego/turn/right" method="POST"><input type="submit" name="turnright" value="TURN RIGHT" onclick="turndegrees(1)"></form>
                </li>
            </ul>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/gh/tomik23/circular-progress-bar@latest/docs/circularProgressBar.min.js"></script>
    <script src="script.js"></script>
</html>
