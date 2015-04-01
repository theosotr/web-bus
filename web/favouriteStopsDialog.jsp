<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8"%>
<%@ page import="gr.dmst.ISIA.app.User" %>
<%@ page import="gr.dmst.ISIA.app.Stop" %>
<%@ page import="java.util.List" %>
<div id="favouriteStops" title="Αγαπημένες στάσεις">
  <%
    String username = session.getAttribute("user").toString();
    User user = new User(username);
    List<Stop> favouriteStops = user.getFavoriteStops();
    int counter = 1;
    if (favouriteStops.isEmpty()) {
  %>
  <p>Δεν έχουν προστεθεί αγαπημένες στάσεις προς το παρών</p>
  <%} else { %>
  <div id="stops">
    <p>Λίστα αγαπημένων στάσεων<br>Επιλέξτε μία από τις παρακάτω στάσεις</p>
    <%for (Stop stop: favouriteStops) { %>
    <input class="<%="stop" + counter%>" type="radio" id='<%="stop" + counter%>' name="favouriteStop" checked="checked" value='<%=stop.getCoordinates()[0] + ", " + stop.getCoordinates()[1]%>'>
    <label style="width: 25em; font-size: 11px;" class="<%="stop" + counter%>" for='<%="stop" + counter%>'><%=stop.getStopName()%> <%=stop.getLocation()%></label>
    <button class="<%="stop" + counter%>" onclick="deleteStopFromFavourites(<%=counter%>)" style="width: 3em;">
      <span class='ui-icon ui-icon-circle-minus' style="float: left;"></span>
    </button><br class="<%="stop" + counter%>">
    <%
        counter++;
      } %>
  </div>
  <% } %>
</div>
