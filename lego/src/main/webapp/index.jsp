<html>
    <head>
        <link rel="stylesheet" href="styles.css">
        <title>Lego Controller</title>
    </head>
    <body>
        <h1>Online EV3 Controller</h1>

        <ul>
            <li>
                <form action="./rest/lego/speedup" method="POST"><input type="submit" name="speedup" value="SPEED UP"></form>
                <form action="./rest/lego/slowdown" method="POST"><input type="submit" name="slowdown" value="SLOW DOWN"></form>
            </li>
            <li>
                <form action="./rest/lego/turn/left" method="POST"><input type="submit" name="turnleft" value="TURN LEFT"></form>
                <form action="./rest/lego/turn/right" method="POST"><input type="submit" name="turnright" value="TURN RIGHT"></form>
            </li>
        </ul>

        <h2>Fill in - this form uses FormParameters</h2>
        <form action="./rest/lego/addrobot" method='post'>
        Breed: <input id='speed' type='text' name='speed' value='' placeholder='New prey speed'><br>
        Weight: <input id='turn' type='text' name='turn' value='' placeholder='turn in kilos'><br>
        <input type='submit' name='ok' value='Send'><br>
        </form>


    </body>
</html>
