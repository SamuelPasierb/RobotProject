// Speedometer: https://codepen.io/Tomik23/pen/GRpMZBz

// Constants SPEED
const PIE = document.getElementById("#speedomter");
const CIRCLE = new CircularProgressBar("pie");
const SPEED = document.getElementById("speed");
const SPEED_RANGE = document.getElementById("speedRange");
const SPEED_FORM = document.getElementById("speed-form");
const SPEED_SETTER = document.getElementById("speed-setter");
const SPEED_VALUE = document.getElementById("set-speed");

// Constants LIGHT FOLLOWER
const LINE_FOLLOWER_SWITCH = document.getElementById("line-switch");
const LINE_FORM = document.getElementById("line-form");

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

    // Set velocity
    SPEED.innerText = velocity + " cm/s";

}

SPEED_RANGE.onchange = function () {
    
    // Calculate speed
    calculateSpeed(this.value);
    
    // Sumbit the form
    SPEED_FORM.submit();
}

SPEED_SETTER.onclick = function () {
    
    // Calucate speed
    calculateSpeed(SPEED_VALUE.value);

    // Update speed range
    SPEED_RANGE.value = SPEED_VALUE.value;

}

// Refresh data
setInterval(() => {
    DATA_IFRAME.src = DATA_IFRAME.src;  
}, 1000)