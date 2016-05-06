package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.models.DBConnection;
import com.models.PlaceModel;
import com.models.PlaceSorter;
import com.models.SortModel;
import com.models.UserModel;
import com.models.UserSorter;
import com.models.checkin;

@Path("/")
public class Services {

	/*
	 * @GET
	 * 
	 * @Path("/signup")
	 * 
	 * @Produces(MediaType.TEXT_HTML) public Response signUp(){ return
	 * Response.ok(new Viewable("/Signup.jsp")).build(); }
	 */

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,
			@FormParam("email") String email, @FormParam("pass") String pass) {
	//	UserModel user = UserModel.addNewUser(name, email, pass);
		UserModel user ;
		user = UserModel.getInstance();
		user.addNewUser(name, email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/checkin")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkin(@FormParam("user") String user,
			@FormParam("place") String place) {
		//boolean check = checkin.checkin(user, place);
		checkin check;
		boolean chck;
		check= checkin.getInstance();
		chck=checkin.checkin(user, place);
		
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}

	@POST
	@Path("/likeCheckin")
	@Produces(MediaType.TEXT_PLAIN)
	public String Lcheckin(@FormParam("notifieduser") String user,
			@FormParam("place") String place, @FormParam("user") String user1) {
		//boolean check = checkin.likeCheckIn(user, place, user1);
		checkin check;
		boolean chck;
		check= checkin.getInstance();
		chck=checkin.likeCheckIn(user, place, user1);
		
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}

	@POST
	@Path("/commentCheckin")
	@Produces(MediaType.TEXT_PLAIN)
	public String Ccheckin(@FormParam("notifieduser") String user,
			@FormParam("place") String place,
			@FormParam("comment") String comment,
			@FormParam("user") String user1) {
		//boolean check = checkin.commentCheckIn(user, place, comment, user1);
		checkin check;
		//ArrayList<String> strArr ;
		boolean chck;
		check= checkin.getInstance();
		chck=checkin.commentCheckIn(user, place, comment, user1);
		
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}

	
	@POST
	@Path("/historyofactions")
	@Produces(MediaType.TEXT_PLAIN)
	public String history(@FormParam("user") String user) {
		ArrayList ar = new ArrayList<String>();
		//ar = checkin.HistoryOfActions(user);
		checkin check;
		check= checkin.getInstance();
		ar=check.HistoryOfActions(user);
		System.out.println("array ......." + ar);
		
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();

	}
	
	@POST
	@Path("/showhomepage")
	@Produces(MediaType.TEXT_PLAIN)
	public String showhomepage(@FormParam("user") String user) {
		System.out.println("....j.....size " +"array ......." );
		
		UserModel user4 ;
		user4 = UserModel.getInstance();
		ArrayList ar = new ArrayList<String>();
		System.out.println("....j.....size " + ar.size()+"array ......." + ar);
		
		ar = user4.showhomePage(user);
		System.out.println(".........size " + ar.size()+"array ......." + ar);
		
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();
	}
	@POST
	@Path("/GetAllNotifications")
	@Produces(MediaType.TEXT_PLAIN)
	public String get(@FormParam("user") String user) {
		
		ArrayList arr = new ArrayList<String>();
		checkin check;
		check= checkin.getInstance();
		arr=check.GetAllNotifications(user);
       System.out.println("array get notifi ......." + arr);
		
		JSONObject obj = new JSONObject();
      //  JSONArray obj = new JSONArray();
		for (int i = 0; i < arr.size(); i++) {
			obj.put((i + 1), arr.get(i));
		}
		return obj.toJSONString();
		}
		
	

	
	@POST
	@Path("/responseToNotifications")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("user") String user, @FormParam("place") String place) {
		JSONArray ar = new JSONArray();
		//ArrayList<String> strArr = checkin.respondToNotification(user, place);
		checkin check;
		ArrayList<String> strArr ;
		check= checkin.getInstance();
		strArr=check.respondToNotification(user, place);
		
		
		for (int i = 0; i < strArr.size(); i++) {
			ar.add(strArr.get(i));
		}
		return ar.toJSONString();
	}
	
	@POST
	@Path("/countUsersForEachPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String countUsersForPlace() {
		ArrayList ar = new ArrayList<String>();
		//ar = UserModel.countUsersForEachPlace();
		UserModel user2 ;
		user2 = UserModel.getInstance();
		ar = user2.countUsersForEachPlace();
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();

	}

	@POST
	@Path("/getPlacesForAuser")
	@Produces(MediaType.TEXT_PLAIN)
	public String getplacelist(@FormParam("user") String user) {
		ArrayList ar = new ArrayList<String>();
		//ar = UserModel.getPlacesforAuser(user);
		UserModel user2 ;
		user2 = UserModel.getInstance();
		ar = user2.getPlacesforAuser(user);
		
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put("place " + (i + 1), ar.get(i));
		}
		return json.toJSONString();

	}

	@POST
	@Path("/getPlacesList")
	@Produces(MediaType.TEXT_PLAIN)
	public String getpPlacelist() {
		ArrayList ar = new ArrayList<String>();
		//ar = UserModel.getUsersforAplace();
		UserModel user2 ;
		user2 = UserModel.getInstance();
		ar = user2.getUsersforAplace();
		
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();

	}
	
	
	@POST
	@Path("/Placesorter")
	@Produces(MediaType.TEXT_PLAIN)
	public String sorter1(@FormParam("user") String user) {
		ArrayList ar = new ArrayList<String>();
		SortModel s = new PlaceSorter();
		ar = s.sorthomePage(user);
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();

	}
	@POST
	@Path("/Usersorter")
	@Produces(MediaType.TEXT_PLAIN)
	public String sorter2(@FormParam("user") String user) {
		ArrayList ar = new ArrayList<String>();
		SortModel s = new UserSorter();
		ar = s.sorthomePage(user);
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put((i + 1), ar.get(i));
		}
		return json.toJSONString();

	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email,
			@FormParam("pass") String pass) {
		//UserModel user = UserModel.login(email, pass);
		UserModel user ;
		user = UserModel.getInstance();
		user=UserModel.login(email, pass);
	
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/updatePosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@FormParam("id") String id,
			@FormParam("lat") String lat, @FormParam("long") String lon) {
		Boolean status = UserModel.updateUserPosition(Integer.parseInt(id),
				Double.parseDouble(lat), Double.parseDouble(lon));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getJson() {
		return "Hello after editing";
		// Connection URL:
		// mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/
	}

	@POST
	@Path("/unfollow")
	@Produces(MediaType.TEXT_PLAIN)
	public String unFollow(@FormParam("id1") int id_follower,
			@FormParam("id2") int id_following) {
		//UserModel.unFollow(id_follower, id_following);
		UserModel user ;
		user = UserModel.getInstance();
	    user.unFollow(id_follower, id_following);
	
	
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}
	@POST
	@Path("/saveplace")
	@Produces(MediaType.TEXT_PLAIN)
	public String savePlace(@FormParam("user") String user,
			@FormParam("place") String place) {
	//	boolean savePPlace = PlaceModel.savePlace(user, place);
		PlaceModel savePlace ; boolean u ;
		savePlace=PlaceModel.getInstance(); 
		u=savePlace.savePlace(user, place);
		
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}

	@POST
	@Path("/addPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String addPlace(@FormParam("name") String name,
			@FormParam("descr") String descr) {
		//boolean place = PlaceModel.addPlace(name, desc);
		PlaceModel savePlace ; boolean u ;
		savePlace=PlaceModel.getInstance(); 
		u=savePlace.addPlace(name, descr);
		
		JSONObject json = new JSONObject();
		json.put("done ", "done ");
		return json.toJSONString();
	}

	@POST
	@Path("/follow")
	@Produces(MediaType.TEXT_PLAIN)
	public String follow(@FormParam("id1") int id_follower

	, @FormParam("id2") int id_following) {
		//UserModel user = UserModel.follow(id_follower, id_following);
		UserModel user ;
		user = UserModel.getInstance();
		user= user.follow(id_follower, id_following);
		JSONObject json = new JSONObject();
		json.put("name", user.getName());
		return json.toJSONString();
	}

	@POST
	@Path("/getFollowingList")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFollowingList(@FormParam("id") int id_follower) {
		ArrayList ar = new ArrayList<String>();
		//ar = UserModel.getFollowingList(id_follower);
		UserModel user ;
		user = UserModel.getInstance();
		ar=user.getFollowingList(id_follower);
		JSONObject json = new JSONObject();
		for (int i = 0; i < ar.size(); i++) {
			json.put("name " + i, ar.get(i));

		}

		return json.toJSONString();

	}

	@POST
	@Path("/getLastPosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLastPosition(@FormParam("id1") int id_follower) {
		//UserModel user = UserModel.getLastPosition(id_follower);
		UserModel user ;
		user = UserModel.getInstance();
		user=user.getLastPosition(id_follower);
		
		JSONObject json = new JSONObject();
		json.put("name", user.getName());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());

		return json.toJSONString();
	}
}
