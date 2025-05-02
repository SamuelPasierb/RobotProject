// Speedometer: https://codepen.io/Tomik23/pen/GRpMZBz

// Constants
const PIE = document.getElementById("#speedomter");
const CIRCLE = new CircularProgressBar("pie");
const SPEED = document.getElementById("speed");
const SPEED_RANGE = document.getElementById("speedRange");
const FORM = document.getElementById("form")
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

SPEED_RANGE.oninput = function () {
    calculateSpeed(this.value);
    FORM.submit();
}