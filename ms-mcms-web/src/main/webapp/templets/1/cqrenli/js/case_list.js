var case_hover;
$(document).ready(function(){
	$('.listimg,.listcase_name').hover(function(){
		if($(this).attr("class") == "listcase_name"){
			$(this).parent().siblings(".listimg").children(".hover_layer").show();
			$(this).siblings(".view_btn").css("background-position","-3px -25px");
			$(this).children("a").addClass("underline");
		}else{
			$(".hover_layer", this).show();
			$(this).siblings(".listinfo").children(".view_btn").css("background-position","-3px -25px");
			$(this).siblings(".listinfo").children(".listcase_name").children("a").addClass("underline");
		}
	}, function() {
		if($(this).attr("class") == "listcase_name"){
			$(this).parent().siblings(".listimg").children(".hover_layer").hide();
			$(this).siblings(".view_btn").css("background-position","-3px -3px");
			$(this).children("a").removeClass("underline");
		}else{
			$(".hover_layer", this).hide();
			$(this).siblings(".listinfo").children(".view_btn").css("background-position","-3px -3px");
			$(this).siblings(".listinfo").children(".listcase_name").children("a").removeClass("underline");
		}
	});
});