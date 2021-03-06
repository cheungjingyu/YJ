<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>首页</title>
<script type="text/javascript">
$(function(){
	  $.ajax({type:"post",url:"<%=ctxPath%>/Index",
		  data:{action:"getCurrentCity"},
		  success:function(result){
			  if(result.status=="ok"){
				  $("#cityName").text(result.data);
			  }else{
				  alert(result.msg);
			  }
		  },
		  error:function(){alert("出现网络错误");}
	  });
	  $("#changeCity").click(function(){
		  var left=$(this).offset().left;
		  var top=$(this).offset().top;
		  var height=$(this).height();
		  $("#cityList").toggle("fast").css("top",top+height).css("left",left);
	  });
	  $("#cityList li").click(function(){
		  var cityId=$(this).attr("cityId");
		  $.ajax({type:"post",url:"<%=ctxPath%>/Index",
			   data:{action:"changeCity",cityId:cityId},
			   success:function(result){
				   if(result.status=="ok"){
					   location.reload();
				   }else{
					   alert(result.msg);
				   }
			   },
			   error:function(){alert("出现网络错误");}
			  });
	  });
	  $("#textSerach").keydown(function(e){
		  if(e.keyCode==13){
			  location.href="<%=ctxPath%>/House?action=search&keyWords="+encodeURI($(this).val());
		  }
	  });
	
});

</script>
</head>
<%@include file="/WEB-INF/loading.jsp" %>
<body>
<body>
  <div id="cityList" style="display: none;position: absolute;z-index: 300;background-color: blue;cursor: pointer;" >
    <ul>
     <c:forEach items="${cities}" var="city">
      <li cityId="${city.id}" >${city.name}</li>
     </c:forEach>
    </ul>
  </div>

		<!--header star-->
		<div class="header clearfloat">
			<div class="tu clearfloat">
				<img src="img/index-banner.jpg"/>
			</div>
			<header class="mui-bar mui-bar-nav">
				<a class="btn" href="javascript:void(0)" id="changeCity">		            
		            <p><i id="cityName"></i><i class="iconfont icon-iconfontarrowdown-copy"></i></p>
		        </a>
		        <div class="top-sch-box flex-col">
		            <div class="centerflex">
		                <i class="fdj iconfont icon-sousuo"></i>
		                <div class="sch-txt">请输入您要搜索的内容</div>
		            </div>
		        </div>
		    </header>
		    <div class="header-tit clearfloat">
		    	<p class="header-num">掌上租已为<span>${totalCount}</span>用户成功租房！</p>
		    	<p class="header-da">轻松租房  乐享生活</p>
		    </div>
		</div>
		<!--header end-->
		<div id="main" class="mui-clearfix">
		 	<!-- 搜索层 -->
		    <div class="pop-schwrap">
		        <div class="ui-scrollview">
		        	<div class="poo-mui clearfloat box-s">
		        		<div class="mui-bar mui-bar-nav clone poo-muitwo">
			                <div class="top-sch-box flex-col">
			                    <div class="centerflex">
			                    	<i class="fdj iconfont icon-sousuo"></i>
			                        <input class="sch-input mui-input-clear" type="text" name="" id="textSerach" placeholder="请输入您要搜索的内容" />
			                    </div>
			                </div>			                
			            </div>
			            <a href="javascript:;" class="mui-btn mui-btn-primary btn-back" href="#">取消</a>
		        	</div>		            
		            <div class="scroll-wrap">
		                <div class="mui-scroll">
		                    <div class="sch-cont">
		                        <div class="section ui-border-b">
		                            <div class="tit">热门搜索</div>
		                            <div class="tags">
		                                <span class="tag">大溪地</span><span class="tag">大溪地</span><span class="tag">大溪地</span>
		                                <span class="tag">大溪地</span><span class="tag">大溪地</span><span class="tag">大溪地</span>
		                                <span class="tag">大溪地</span><span class="tag">大溪地</span><span class="tag">大溪地</span>
		                            </div>
		                        </div>
		                    </div>
		                </div>
		            </div>
		        </div>
		    </div>
		    
		    <div class="cation clearfloat">
		    	<ul class="clearfloat">
		    		<li>
		    			<a href="entire.html">
			    			<img src="img/fang.png" />
			    			<p>整租</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="entire.html">
			    			<img src="img/chuang.png" />
			    			<p>合租</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="entire.html">
			    			<img src="img/bao.png" />
			    			<p>短租</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="entire.html">
			    			<img src="img/lou.png" />
			    			<p>写字楼</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="map.html">
			    			<img src="img/map.png" />
			    			<p>地图找房</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="landlord.html">
			    			<img src="img/people.png" />
			    			<p>我是房东</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="join.html">
			    			<img src="img/woshou.png" />
			    			<p>加盟</p>
		    			</a>
		    		</li>
		    		<li>
		    			<a href="life-service.html">
			    			<img src="img/self.png" />
			    			<p>生活服务</p>
		    			</a>
		    		</li>
		    	</ul>
		    </div>
		    
		    <div class="recom clearfloat">
		    	<div class="recom-tit clearfloat box-s">
		    		<p>热门房源推荐</p>
		    	</div>
		    	<div class="content clearfloat box-s">
		    		<div class="list clearfloat fl box-s">
		    			<a href="house-details.html">
			    			<div class="tu clearfloat">
			    				<span></span>
			    				<img src="upload/list-tu.jpg"/>
			    			</div>
			    			<div class="right clearfloat">
			    				<div class="tit clearfloat">
			    					<p class="fl">华府骏苑</p>
			    					<span class="fr">2500<samp>元/月</samp></span>
			    				</div>
			    				<p class="recom-jianjie">三室一厅一卫   |  125m²  |  普装</p>
			    				<div class="recom-bottom clearfloat">
			    					<span><i class="iconfont icon-duihao"></i>随时住</span>
			    					<span><i class="iconfont icon-duihao"></i>家电齐全</span>
			    				</div>
			    			</div>
		    			</a>
		    		</div>
		    		<div class="list clearfloat fl box-s">
		    			<a href="house-details.html">
			    			<div class="tu clearfloat">
			    				<span></span>
			    				<img src="upload/list-tu.jpg"/>
			    			</div>
			    			<div class="right clearfloat">
			    				<div class="tit clearfloat">
			    					<p class="fl">华府骏苑</p>
			    					<span class="fr">2500<samp>元/月</samp></span>
			    				</div>
			    				<p class="recom-jianjie">三室一厅一卫   |  125m²  |  普装</p>
			    				<div class="recom-bottom clearfloat">
			    					<span><i class="iconfont icon-duihao"></i>随时住</span>
			    					<span><i class="iconfont icon-duihao"></i>家电齐全</span>
			    				</div>
			    			</div>
		    			</a>
		    		</div>
		    		<div class="list clearfloat fl box-s">
		    			<a href="house-details.html">
			    			<div class="tu clearfloat">
			    				<span></span>
			    				<img src="upload/list-tu.jpg"/>
			    			</div>
			    			<div class="right clearfloat">
			    				<div class="tit clearfloat">
			    					<p class="fl">华府骏苑</p>
			    					<span class="fr">2500<samp>元/月</samp></span>
			    				</div>
			    				<p class="recom-jianjie">三室一厅一卫   |  125m²  |  普装</p>
			    				<div class="recom-bottom clearfloat">
			    					<span><i class="iconfont icon-duihao"></i>随时住</span>
			    					<span><i class="iconfont icon-duihao"></i>家电齐全</span>
			    				</div>
			    			</div>
		    			</a>
		    		</div>
		    	</div>
		    </div>
	   </div>
	   
	   <footer class="page-footer fixed-footer" id="footer">
			<ul>
				<li class="active">
					<a href="index.html">
						<i class="iconfont icon-shouyev1"></i>
						<p>首页</p>
					</a>
				</li>
				<li>
					<a href="lease.html">
						<i class="iconfont icon-chuzuwo"></i>
						<p>我要出租</p>
					</a>
				</li>
				<li>
					<a href="schedule.html">
						<i class="iconfont icon-richengbiao"></i>
						<p>看房日程</p>
					</a>
				</li>
				<li>
					<a href="<%=ctxPath%>/User?action=center">
						<i class="iconfont icon-gerenzhongxin"></i>
						<p>个人中心</p>
					</a>
				</li>
			</ul>
		</footer>
	</body>
	
	<script src="js/fastclick.js"></script>
	<script src="js/mui.min.js"></script>
	<script type="text/javascript" src="js/hmt.js" ></script>
	<!--插件-->
	<link rel="stylesheet" href="css/swiper.min.css">
	<script src="js/swiper.jquery.min.js"></script>
	<!--插件-->

	<!--搜索点击效果-->
	<script >
	    $(function () {
	        var banner = new Swiper('.banner',{
	            autoplay: 5000,
	            pagination : '.swiper-pagination',
	            paginationClickable: true,
	            lazyLoading : true,
	            loop:true
	        });
	
	         mui('.pop-schwrap .sch-input').input();
	        var deceleration = mui.os.ios?0.003:0.0009;
	         mui('.pop-schwrap .scroll-wrap').scroll({
	                bounce: true,
	                indicators: true,
	                deceleration:deceleration
	        });
	        $('.top-sch-box .fdj,.top-sch-box .sch-txt,.pop-schwrap .btn-back').on('click',function () {
	            $('html,body').toggleClass('holding');
	            $('.pop-schwrap').toggleClass('on');
	            if($('.pop-schwrap').hasClass('on')) {;
	                $('.pop-schwrap .sch-input').focus();
	            }
	        });
	
	    });
	</script>
</body>
</html>