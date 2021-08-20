let rejectId = document.getElementById("rejectDiv").value;
let popForm = document.getElementById("popForm").action = "/rejectOrder(orderId=${order.id})}";

function openForm() {
    document.getElementById("myForm").style.display = "block";
}

function closeForm() {
    document.getElementById("myForm").style.display = "none";
}