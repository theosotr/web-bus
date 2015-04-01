<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8"%>
<%@ page import="gr.dmst.ISIA.app.User" %>
<%@ page import="gr.dmst.ISIA.app.Route" %>
<%@ page import="java.util.List" %>
<div id="favouriteRoutes" title="Αγαπημένες στάσεις">
  <%
  	String username = session.getAttribute("user").toString();
    User user = new User(username);
    List<Route> favouriteRoutes = user.getFavouriteRoutes();
    if(favouriteRoutes.isEmpty()) {
  %>
  <p>Δεν έχουν προστεθεί αγαπημένες γραμμές προς το παρόν</p>
  <%} else { %>
  <div id="routes">
    <p>Λίστα αγαπημένων στάσεων<br>Επέλεξε μία από τις παραπάνω γραμμές</p>
    <%for (Route route: favouriteRoutes) {
      String number = route.getRouteNumber();
    %>
    <input class="<%="route" + number%>" type="radio" id='<%="route" + number%>' name="favouriteRoute" checked="checked" value='<%=route.getRouteNumber() + ", " + route.getRouteName()%>'>
    <label style="width:25em;" for='<%="route" + number%>' class="<%="route" + number%>"><%=route.getRouteNumber() + ", " + route.getRouteName()%></label>
    <button class="<%="route" + number%>" onclick="deleteRouteFromFavourites('<%=number%>')" style="width: 3em;">
      <span class='ui-icon ui-icon-circle-minus' style="float: left;"></span>
    </button><br class="<%="route" + number%>">
    <%
      } %>
  </div>
  <%} %>
</div>
