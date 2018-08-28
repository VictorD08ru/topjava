var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
                "asc"
            ]
        ]
    });
    makeEditable();
    $('.enable').click(function(){
        var id = $(this).closest("tr").attr("id");
        var str = $(this).is(':checked') ? "enabled" : "disabled";
            $.ajax({
            url: ajaxUrl + id,
            method: 'POST',
            success: function(){
                updateTable();
                successNoty("User " + str);
            }
        });
    });
});