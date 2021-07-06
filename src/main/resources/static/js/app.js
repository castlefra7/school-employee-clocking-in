const URL = "http://localhost:8080/stats-payment-by-date";

function ajaxExample() {
    fetch(URL)
        .then((res) => res.json())
        .then((data) => console.log(data));
}

$('#btn-update').click(function () {
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        const id_emp = $("#id").val();
        const id_semaine = $("#id-semaine-update").val();

        const url = `http://localhost:8080/employees-pointage-update-front?id-semaine=${id_semaine}&id-emp=${id_emp}`;

        $.ajax({
            url: url,
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data) {
                    if (data.status) {
                        if (data.status.code == 200) {
                            let d = data.data[0];
                            d.forEach((dd) => {
                                $(`#${dd.weekOfDay}-ferier`).val(dd.numberHoursFerier);
                                $(`#${dd.weekOfDay}-day`).val(dd.numberHoursDaily);
                                $(`#${dd.weekOfDay}-night`).val(dd.numberHoursNightly);
                            })
                        } else {
                            alert(data.status.message);
                        }
                    }
                }
            },
        });
    });
});

$('#validate-hours').click(function () {
    console.log("Calculating hours");
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        const url = "http://localhost:8080/employees-pointage-validate-front/";
        const id_emp = $("#id").val();
        const id_semaine = $("#id-semaine").val();

        const numberDays = 7;
        const pointings = [];
        for (let iD = 1; iD <= numberDays; iD++) {
            pointings.push({
                // isHoliday:$(`#${iD}-ferier`).is(":checked"),
                isHoliday: false,
                numberHoursFerier: $(`#${iD}-ferier`).val(),
                weekOfDay: iD,
                numberHoursDaily: $(`#${iD}-day`).val(),
                numberHoursNightly: $(`#${iD}-night`).val(),
                id_semaine: id_semaine,
                id_employee: id_emp
            });
            // console.log($(`#${iD}-ferier`).is(":checked"));
            // console.log($(`#${iD}-day`).val());
            // console.log($(`#${iD}-night`).val());
        }


        const data = {
            pointings: pointings,
            employee: {
                id: id_emp
            },
            semaine: id_semaine
        };

        $.ajax({
            url: url,
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.log(data);
                if (data) {
                    if (data.status) {
                        if (data.status.code == 200) {
                            window.location.href = 'http://localhost:8080/employees-fiche-front/' + id_emp;
                        } else {
                            alert(data.status.message);
                        }
                    }
                }
            },
        });
    });
})


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
                // isHoliday:$(`#${iD}-ferier`).is(":checked"),
                isHoliday: false,
                numberHoursFerier: $(`#${iD}-ferier`).val(),
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
                if (data.status.code == 200) {
                    console.table(data.data[0]);
                    const hoursData = data.data[0];
                    $("#table-hours tbody tr").empty();
                    hoursData.forEach((hourData) => {
                        $('#table-hours > tbody:last-child').append(`<tr><td>${hourData.code}</td><td>${hourData.hours}</td></tr>`);
                    });
                } else {
                    alert(data.status.message);
                }
                
            },
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {

    // Get all "navbar-burger" elements
    const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

    // Check if there are any navbar burgers
    if ($navbarBurgers.length > 0) {

        // Add a click event on each of them
        $navbarBurgers.forEach(el => {
            el.addEventListener('click', () => {

                // Get the target from the "data-target" attribute
                const target = el.dataset.target;
                const $target = document.getElementById(target);

                // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
                el.classList.toggle('is-active');
                $target.classList.toggle('is-active');

            });
        });
    }



});