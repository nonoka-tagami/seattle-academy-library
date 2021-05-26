$(function(){
	
	var btn = $('#buttonCheck1').val();

	if (btn === '貸出可') {
		$('.btn_returnBook').prop('disabled', true);
	} else {
		$('.btn_rentBook').prop('disabled', true);
		$('.btn_deleteBook').prop('disabled', true);
	}
});
