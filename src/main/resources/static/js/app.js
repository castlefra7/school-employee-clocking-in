const URL = "http://localhost:8080/stats-payment-by-date";

function ajaxExample() {
    fetch(URL)
        .then((res) => res.json())
        .then((data) => console.log(data));
}




$("#calculate-hours").click(function (event) {
    console.log("Calculating hours");
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        const url = "http://localhost:8080/employees-pointage-front/";
        const id_emp = $("#id").val();

        const numberDays = 7;
        const pointings = [];
        for (let iD = 1; iD <= numberDays; iD++) {
            pointings.push({
                isHoliday:$(`#${iD}-ferier`).is(":checked"),
                weekOfDay: iD,
                numberHoursDaily: $(`#${iD}-day`).val(),
                numberHoursNightly: $(`#${iD}-night`).val()
            });
            // console.log($(`#${iD}-ferier`).is(":checked"));
            // console.log($(`#${iD}-day`).val());
            // console.log($(`#${iD}-night`).val());
        }

        const data = {
            pointings: pointings,
            employee: {
                id: id_emp
            }
        };

        $.ajax({
            url: url,
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.table(data.data[0]);
                const hoursData = data.data[0];
                $("#table-hours tbody tr").empty();
                hoursData.forEach((hourData) => {
                    $('#table-hours > tbody:last-child').append(`<tr><td>${hourData.code}</td><td>${hourData.hours}</td></tr>`);
                });
            },
        });
    });
});


// var element = document.getElementById('element-to-print');
// html2pdf(element);


// [
//     {
//         isHoliday: false,
//         weekOfDay: 1,
//         numberHoursDaily: 10,
//         numberHoursNightly: 5,
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 2,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 3,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0,
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 4,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 5,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0,
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 6,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0
//     },
//     {
//         isHoliday: false,
//         weekOfDay: 7,
//         numberHoursDaily: 10,
//         numberHoursNightly: 0,
//     }
// ]