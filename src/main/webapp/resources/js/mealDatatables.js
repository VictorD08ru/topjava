var ajaxUrl = "ajax/meals/";
var datatableApi;

var startDate;
var endDate;
var startTime;
var endTime;

$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
    setFilterForm();
});

function filter() {
    var form = $('#mealFilter');
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: form.serialize(),
        success: function () {
            successNoty("Filtered");
    }
    });
    startDate = $("#startDate").val();
    endDate = $("#endDate").val();
    startTime = $("#startTime").val();
    endTime = $("#endTime").val();
    setFilterForm();
}

function setFilterForm() {
    $("#startDate").val(startDate);
    $("#endDate").val(endDate);
    $("#startTime").val(startTime);
    $("#endTime").val(endTime);
}

function clearFilterForm() {
    $("#startDate").val("");
    $("#endDate").val("");
    $("#startTime").val("");
    $("#endTime").val("");
    $.ajax({
        type: "GET",
        url: "ajax/meals",
        success: function () {
            datatableApi.clear();
            successNoty("Filter cleared");
        }
    });
}