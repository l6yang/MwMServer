var docHeight = ''; //
var docWidth = ''; //
function msgShow() {
    $('#overlay')
        .height(docHeight)
        .css({
            'opacity': .5, //
            'position': 'absolute',
            'top': 0,
            'left': 0,
            'background-color': 'black',
            'width': '100%',
            'z-index': 1000,
            'display': 'block'
        });
    $('#overlay1')
        .height(30)
        .css({
            'position': 'absolute',
            'top': docHeight / 2 - 15,
            'left': docWidth / 2 - 150,
            'background-color': '#FFFFFF',
            'border-radius': '5px',
            'line-height': '30px',
            'width': 300,
            'z-index': 5000,
            'display': 'block'
        });
}
function msgHide() {
    $('#overlay').css({'display': 'none'});
    $('#overlay1').css({'display': 'none'});
}