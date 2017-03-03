$.fn.extend({
    resizeDataGrid: function (widthMargin, minWidth) {
        var width = $(document.body).width() - widthMargin;
        width = width < minWidth ? minWidth : width;
        $(this).datagrid('resize', {
            width: width
        });
    }
});
$(function () {
    var datagridId = 'list-date';
// $('#' + datagridId).resizeDataGrid(0, 0);
    $(window).resize(function () {
        $('#' + datagridId).resizeDataGrid(0, '100%');
    });
});