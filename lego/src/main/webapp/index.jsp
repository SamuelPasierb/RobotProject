<html>
    <head>
        <link rel="stylesheet" href="styles.css">
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
                </div>
                <div class="object">
                    
                </div>
            </div>
        </div>

        <div id="right">
            <h1>Robot Controller</h1>

            <ul>
                <li>
                    <h3>0</h3>
                    <form action="./rest/lego/speedup" method="POST" id="form">
                        <input type="range" min="0" max="500" value="0" class="slider" id="speedRange">
                    </form>
                    <h3>500</h3>
                </li>
                <li>
                    <form action="./rest/lego/turn/left" method="POST"><input type="submit" name="turnleft" value="TURN LEFT"></form>
                    <form action="./rest/lego/turn/right" method="POST"><input type="submit" name="turnright" value="TURN RIGHT"></form>
                </li>
                <li>
                    <!-- TODO: sync this with speedo and slider -->
                    <h3>Set Speed Manually</h3>
                    <form action="./rest/lego/setspeed" method="POST"><input id="setspeed" type="text" name="setspeed" value=""><input type="submit" name="ok" value="Send">
                    </form>
                </li>
            </ul>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/gh/tomik23/circular-progress-bar@latest/docs/circularProgressBar.min.js"></script>
    <script src="script.js"></script>
</html>
