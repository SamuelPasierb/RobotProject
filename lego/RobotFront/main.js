$(document).ready(function () {
  $('.speed').click(function () {
    const selectedSpeed = $('.speed:checked').val()

    if (selectedSpeed === 'slow') {
      $('.needle').addClass('slow').removeClass('medium fast')
    } else if (selectedSpeed === 'medium') {
      $('.needle').addClass('medium').removeClass('slow fast')
    } else if (selectedSpeed === 'fast') {
      $('.needle').addClass('fast').removeClass('slow medium')
    }
  })
})

const bulb = document.querySelector('#bulb')
bulb.addEventListener('change', toggleStatus)

function toggleStatus(e) {
  console.log(e)
  console.log(e.target)
  console.log(e.target.checked)
}

const slider = document.querySelector('#dim-value-slider')
const output = document.querySelector('#dim-value')

slider.addEventListener('input', updateDimmerValue)

async function updateDimmerValue(e) {
  //update dimmer value
  output.innerHTML = slider.value

  //send request
  let url = '/dimmer/change' + '?status=' + slider.value
  console.log('Sending to ' + url)

  // let response = await sendRequestToServer(url);

  // console.log(response);
}

// Prevent scrolling on every click!

// super sweet vanilla JS delegated event handling!
document.body.addEventListener('click', function (e) {
  if (e.target && e.target.nodeName == 'A') {
    e.preventDefault()
  }
})

function touchStartHandler(event) {
  console.log(event.target.dataset.direction)
  console.log('Touch Start!')
  // alert("Click!")
}

function touchEndHandler(event) {
  console.log(event.target.name)
  console.log('Touch End!')
  // alert("Click!")
}

document.querySelectorAll('.control').forEach((item) => {
  item.addEventListener('touchstart', touchStartHandler)
})

document.querySelectorAll('.control').forEach((item) => {
  item.addEventListener('touchend', touchEndHandler)
})

var speedSettings = document.querySelectorAll(
  'input[type=radio][name="speed-settings"]'
)
speedSettings.forEach((radio) =>
  radio.addEventListener('change', () => alert(radio.value))
)
