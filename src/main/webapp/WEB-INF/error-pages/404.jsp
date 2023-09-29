<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
  isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" type="image/png" sizes="16x16"
  href='<c:url value="${pageContext.request.contextPath}/assets/plugins/images/favicon.png"></c:url>'>
<title>404 - Not Found</title>
<!-- Bootstrap Core CSS -->
<link href='${pageContext.request.contextPath}/bootstrap/dist/css/bootstrap.min.css'
  rel="stylesheet">
<!-- animation CSS -->
<link href='${pageContext.request.contextPath}/css/animate.css' rel="stylesheet">
<!-- Custom CSS -->
<link href='${pageContext.request.contextPath}/css/style.css' rel="stylesheet">
<!-- color CSS -->
<link href='${pageContext.request.contextPath}/css/colors/blue.css' id="theme" rel="stylesheet">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body>
  <!-- Preloader -->
  <div class="preloader">
    <div class="cssload-speeding-wheel"></div>
  </div>
  <section id="wrapper" class="error-page">
    <div class="error-box">
      <div class="error-body text-center">
        <h1>404</h1>
        <h3 class="text-uppercase">Page not found!</h3>
        <p class="text-muted m-t-30 m-b-30">Please check again.</p>
        <a href='<c:url value="/" />'
          class="btn btn-info btn-rounded waves-effect waves-light m-b-40">Back to home</a>
      </div>
      <footer class="footer text-center">2018 © Pixel Admin.</footer>
    </div>
  </section>
  <!-- jQuery -->
  <script
    src='${pageContext.request.contextPath}/plugins/bower_components/jquery/dist/jquery.min.js'></script>
  <!-- Bootstrap Core JavaScript -->
  <script src='${pageContext.request.contextPath}/bootstrap/dist/js/bootstrap.min.js'></script>
  <script type="text/javascript">
      $(function() {
        $(".preloader").fadeOut();
      });
    </script>
</body>

</html>