<%@ page language="java" pageEncoding="UTF-8"%>
<!-- <div class="content-right" data-spm="2"> -->
    <div class="bread-crumb">
        <span class="item"><a href="#">通用云引擎GCE</a></span>&nbsp;&gt;&nbsp;<span class="data-spm-click" data-id="5-1-2">GCE基础架构</span>
    </div>
    <h2 style="text-align: center;color: #333;font-size: 18px;">GCE基础架构</h2>
	<div class="article-content">
		<div class="markdown-body ng-scope" ui-view="doc"
			style="margin: 0 10px; margin-bottom: 25px;">
			<div id="doc" doc-preloader="isload"
				style="padding-top: 10px; min-height: 400px; font-size: 14px; line-height: 26px;"
				class="ng-scope doc-preloader-hide">
				<url class="doc-menu"></url>
				<div class="intro">GCE的系统架构从逻辑上可分为三部分：管理系统、运行系统和扩展服务。这些系统全部搭建在乐视通用云的大规模分布式计算系统上。管理系统负责整个平台的管控，包括资源生产、状态监控、弹性调度、数据存储、提供管理接口等；运行系统负责为托管的网络应用提供路由、计算、网络资源以及运行所需的中间件（如Web服务容器、负载均衡等），运行系统实现了多层沙箱隔离机制，保障不同租户的计算资源和数据的安全性；扩展服务框架负责接入各种常用的分布式云服务，旨在减少开发者重复开发通用功能，提高开发效率，并提供统一的使用接口。示意图如下：</div>
				<p><img src="${ctx}/static/img/help/product/5.jpg" alt=""></p>
				<ul>
					<li><p>调度中心</p>
						<p>
							根据监控中心采集到的计算资源的负载、网络流量等情况，分析各个网络应用的冷热状况，调整应用所占有的运行资源和运行实例的负载均衡策略。</p></li>
					<li><p>资源中心</p>
						<p>负责向运行系统发送生产、修改、删除、迁移等运行资源相关的指令。</p></li>
					<li><p>监控中心</p>
						<p>实时收集管理系统和运行系统中各个模块及运行实例的运行情况指标。</p></li>
					<li><p>日志中心</p>
						<p>采集汇总应用的运行实例输出的日志并存储，提供查询分析功能。</p></li>
					<li><p>审计中心</p>
						<p>负责记录和统计系统中发生的关键操作，如对应用的管理、对资源的调度等。</p></li>
					<li><p>代码仓库</p>
						<p>负责保存用户所上传的应用的代码文件及版本信息。</p></li>
					<li><p>接入层</p>
						<p>
							负责接收来自互联网的访问请求，根据请求中携带的应用的唯一标识，转发到下层运行环境中该应用所对应的服务实例。服务实例处理完请求后向接入层返回相应内容，接入层再返回给访问发起的访问请求的地址。</p>
					</li>
					<li><p>运行环境</p>
						<p>
							由若干的Web服务器组成，是GCE中实际处理访问请求的模块。运行环境中，每个Web服务器上都安装了应用运行所必须的中间件（如PHP、Nginx、JRE、Tomcat），运行实例即是运行在这些中间件之上。运行环境还提供了沙箱环境，每个运行实例都运行在沙箱之中，不同的实例之间互不干扰。
						</p></li>
					<li><p>运行实例</p>
						<p>
							运行实例是运行环境的最小运行单元，是一个进程。一个应用可以有多个对等实例，入口通过接入层做负载均衡。每个运行实例运行在GCE的沙箱之中。</p>
					</li>
					<li><p>扩展服务</p>
						<p>提供常用的分布式云服务，开发者可以通过GCE提供的SDK方便地调用。</p></li>
					<li><p>安全机制</p>
						<p>应用运行环境的沙箱结构示意图如下：</p></li>
				</ul>
				<p><img src="${ctx}/static/img/help/product/6.jpg"alt=""></p>
				<ul>
					<li><p>资源层沙箱</p>
						<p>对用户和运行实例占有的资源进行隔离。</p></li>
					<li><p>操作系统层沙箱</p>
						<p>控制文件的读写权限。仅允许读写用户自己的文件，禁止执行任何程序，禁止对敏感系统文件的读取。</p></li>
					<li><p>运行实例沙箱</p>
						<p>对开发语言的调用进行限制，禁止使用禁用的类、方法。</p></li>
					<li><p>扩展服务</p>
						<p>Session、MySQL、缓存、存储、定时任务、后台任务、页面抓取、Mongodb、Redis。</p></li>
				</ul>
			</div>
		</div>
	</div>
<!-- </div> -->
<script>
var target=$('.data-spm-click').attr('data-id');
$('.vnavbar').find('a').removeClass('current');
$('.vnavbar').find('li[data-id='+target+']').children('a').addClass('current');
$('li[data-id='+target+']').closest('ul').removeClass('hidden');
$('li[data-id='+target+']').closest('ul').prev().find('span').removeClass('arrow-down').addClass('arrow-up');
</script>