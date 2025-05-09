// Speedometer: https://codepen.io/Tomik23/pen/GRpMZBz

// Constants SPEED
const PIE = document.getElementById("#speedomter");
const CIRCLE = new CircularProgressBar("pie");
const SPEED = document.getElementById("speed");
const SPEED_RANGE = document.getElementById("speedRange");
const SPEED_FORM = document.getElementById("speed-form");
const SPEED_SETTER = document.getElementById("speed-setter");
const SPEED_VALUE = document.getElementById("set-speed");
const AVERAGE_SPEED = document.getElementById("average-speed");
const SPEEDS = [];

// Constants LIGHT FOLLOWER
const LINE_FOLLOWER_SWITCH = document.getElementById("line-switch");
const LINE_FORM = document.getElementById("line-form");

// Constants OBSTACLE AVOIDANCE
const AVOIDANCE_FORM = document.getElementById("avoidance-form");

// Constaints TURNS
const TURN = document.getElementById("turn-degrees");

// Constats DATA
const DATA_IFRAME = document.getElementById("data-iframe");

// Constants CALCULATIONS
const WHEEL_SIZE = 5.6;
const MAX_POWER = 500;

// Create speedometer
window.addEventListener("DOMContentLoaded", () => {
    CIRCLE.initial(PIE);
});  


// Updates speedometer after speed changes
function updateSpeedometerPie(p) {
    const options = {
        index: 1,
        percent: p
    };
    CIRCLE.animationTo(options);
}

// Calculates speed
function calculateSpeed(wheelPower) {
    
    // Calculate percentage
    var p = wheelPower / MAX_POWER * 100;

    // Update the speedometer
    updateSpeedometerPie(p);

    // Calculate velocity
    var velocity = (WHEEL_SIZE * wheelPower / 60).toFixed(2);

    // For average speed
    SPEEDS.push(Number.parseInt(velocity));

    // Set velocity
    SPEED.innerText = velocity + " cm/s";

}

// Speed range
SPEED_RANGE.onchange = function () {
    
    // Calculate speed
    calculateSpeed(this.value);
    
    // Sumbit the form
    SPEED_FORM.submit();
}

// Manual speed
SPEED_SETTER.onclick = function () {
    
    // Calucate speed
    calculateSpeed(SPEED_VALUE.value);

    // Update speed range
    SPEED_RANGE.value = SPEED_VALUE.value;

}

// Obstacle avoidance form
function avoidanceForm() {
    AVOIDANCE_FORM.submit();
}

// Refresh data
setInterval(() => {

    // Update sensor data from the robot
    DATA_IFRAME.src = DATA_IFRAME.src;  

    // Average speed
    if (SPEEDS.length > 10) {
        var sum = SPEEDS.reduce((a, b) => a + b, 0); // Sum
        AVERAGE_SPEED.innerText = "Average speed: " + (sum / SPEEDS.length).toFixed(2) + "  cm/s"; // Set average
    }
}, 1000)