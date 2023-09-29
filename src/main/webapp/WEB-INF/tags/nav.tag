<%@tag description="Navigation" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top m-b-0">
  <div class="navbar-header">
    <a class="navbar-toggle hidden-sm hidden-md hidden-lg" href="javascript:void(0)"
      data-toggle="collapse" data-target=".navbar-collapse">
      <i class="fa fa-bars"></i>
    </a>
    <div class="top-left-part">
      <a class="logo" href="<c:url value='/'></c:url>">
        <b>
          <img src="plugins/images/pixeladmin-logo.png" alt="home" />
        </b>
        <span class="hidden-xs">
          <img src="plugins/images/pixeladmin-text.png" alt="home" />
        </span>
      </a>
    </div>
    <ul class="nav navbar-top-links navbar-left m-l-20 hidden-xs">
      <li>
        <form role="search" class="app-search hidden-xs">
          <input type="text" placeholder="Search..." class="form-control">
          <a href="">
            <i class="fa fa-search"></i>
          </a>
        </form>
      </li>
    </ul>
    <ul class="nav navbar-top-links navbar-right pull-right">
      <li>
        <div class="dropdown">
          <a class="profile-pic dropdown-toggle" data-toggle="dropdown" href="#">
            <img src="plugins/images/users/varun.jpg" alt="user-img" width="36" class="img-circle" />
            <b class="hidden-xs">${userLoggedIn.email}</b>
          </a>
          <ul class="dropdown-menu">
            <li>
              <a
                href="<c:url value='/update-user'>
              <c:param name = "id" value = "${userLoggedIn.id}"/>
              </c:url>">Update
                information</a>
            </li>
            <li>
              <a
                href="<c:url value='profile'>
              <c:param name = "id" value = "${userLoggedIn.id}"/>
              </c:url>">Profile</a>
            </li>
            <li class="divider"></li>
            <li>
              <a href="<c:url value='/logout'></c:url>">Log out</a>
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
  <!-- /.navbar-header -->
  <!-- /.navbar-top-links -->
  <!-- /.navbar-static-side -->
</nav>
<!-- Left navbar-header -->
<div class="navbar-default sidebar" role="navigation">
  <div class="sidebar-nav navbar-collapse slimscrollsidebar">
    <ul class="nav" id="side-menu">
      <li style="padding: 10px 0 0;">
        <a href="<c:url value='/'></c:url>" class="waves-effect">
          <i class="fa fa-clock-o fa-fw" aria-hidden="true"></i>
          <span class="hide-menu">Dashboard</span>
        </a>
      </li>
      <li>
        <a href="<c:url value='/users'></c:url>" class="waves-effect">
          <i class="fa fa-user fa-fw" aria-hidden="true"></i>
          <span class="hide-menu">Users</span>
        </a>
      </li>
      <li>
        <a href="<c:url value='/roles'></c:url>" class="waves-effect">
          <i class="fa fa-modx fa-fw" aria-hidden="true"></i>
          <span class="hide-menu">Roles</span>
        </a>
      </li>
      <li>
        <a href="<c:url value='/projects'></c:url>" class="waves-effect">
          <i class="fa fa-table fa-fw" aria-hidden="true"></i>
          <span class="hide-menu">Projects</span>
        </a>
      </li>
      <li>
        <a href="<c:url value='/tasks'></c:url>" class="waves-effect">
          <i class="fa fa-table fa-fw" aria-hidden="true"></i>
          <span class="hide-menu">Tasks</span>
        </a>
      </li>
    </ul>
  </div>
</div>
<!-- Left navbar-header end -->