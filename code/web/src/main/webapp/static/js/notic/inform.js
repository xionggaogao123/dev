/**
 * Created by fulaan on 15-1-30.
 */




    $(function(){
        $("#new-inform").click(function(){
            $("#compile_in").css("display","block")
        })
    })


    $(function(){
        $(".infrom-YX").hover(function(){
            $(".valid-time").show();
            $(".valid-timeT").show();
        },function(){
        $(".valid-time").hide();
        $(".valid-timeT").hide();
        })
    })
    $(function(){
        $("#QX").click(function(){
            $(".inform-popup-I").css("display","block")
        })
    })


    $(function(){
        $("#compile-but").click(function(){
            $(".contacts-main").css("display","block")
            $("#bg").css("display","block")
        })
    })

    /*--=================下拉==========================--*/

    $(function(){
        $(".comtacts-sv").click(function(){
            if($(".comtacts-ss").prop('className').indexOf('fa-caret-right') > 0){
                $(".comtacts-ss").addClass("fa-caret-down").removeClass('fa-caret-right');
            }else{
        $(".comtacts-ss").addClass("fa-caret-right").removeClass('fa-caret-down');
        }

    })
    })


    $(document).ready(function(){
        $(".comtacts-ss").click(function(){
            $(".XL").toggle();
        });
    });
    
    
    

    $(function(){
        $(".contacts-top-right").click(function(){
            $(".contacts-main").css("display","none")
            $("#bg").css("display","none")
        })
    })


    $(function(){
        $("#infrom-delete").click(function(){
            $(".inform-popup").css("display","block")
        })
    })


    $(function(){
        $("#infrom-popup-QX").click(function(){
            $(".inform-popup").css("display","none")
        })
    })
    $(function(){
        $("#infrom-bottom-BQ").click(function(){
            $(".inform-popup-I").css("display","none")
        })
    })
    $(function(){
        $("#infrom-bottom-QX").click(function(){
            $(".inform-popup-I").css("display","none")
            $(".compile-main").css("display","none")
        })
    })


