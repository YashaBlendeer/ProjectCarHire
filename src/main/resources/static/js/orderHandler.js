let startDateInput = document.getElementById('startdatetime');
let endDateInput = document.getElementById('enddatetime');
let driverCheckbox = document.getElementById('driver');

startDateInput.addEventListener("change", calcPrice)
endDateInput.addEventListener("change", calcPrice)
driverCheckbox.addEventListener("change", calcPrice)


//TODO solve problem with time set
function getStartTime() {
    let startTime = startDateInput.value;
    let startTimeDate = new Date(startTime).getTime();
    console.log('startTime: ' + startTimeDate);
    return startTimeDate;
}


function getEndTime() {
    let endTime = endDateInput.value;
    let endTimeDate = new Date(endTime).getTime();
    console.log('endTimeDate: ' + endTimeDate);
    return endTimeDate;
}

function calcPrice() {
    if (startDateInput.value !== '' &&
        endDateInput.value !== '') {
        const milliseconds = Math.abs(getEndTime() - getStartTime());
        console.log('getEndTime() - getStartTime(): ' + Math.abs(getEndTime() - getStartTime()));
        const hours = Math.ceil(milliseconds / 36e5);
        console.log(hours);

        let carPrice = document.getElementById('carPrice').value;
        let driverPrice = document.getElementById('driverPrice').value;
        let withDriver = document.getElementById('driver').checked;
        console.log('carPrice: ' + carPrice)
        console.log('driverPrice: ' + driverPrice)

        console.log('withDriver without .vallue: ' + document.getElementById('driver'))
        console.log('withDriver: ' + withDriver)
        let orderPrice = hours*carPrice + hours * (withDriver ? driverPrice : 0);
        document.getElementById('orderprice').value = orderPrice;

    } else {
        document.getElementById('orderprice').value = 'Input start and date time';
    }

}




// function calcMinEndTime() {
//     let minEndTime;
//     minEndTime = getStartTime();
//     minEndTime.setHours(minEndTime.getHours() + 1);
//     console.log('calcMinEndTime: ' + minEndTime);
//     return minEndTime;
// }

