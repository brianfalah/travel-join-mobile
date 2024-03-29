package com.example.traveljoin.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Group implements Serializable, GeneralItem {

	private static Integer PUBLIC = 0;
	private static Integer PRIVATE = 1;

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;
	private Integer type;
	private String password;
	private User owner;
	private Integer ownerId;
	private Boolean joined;

	private ArrayList<GroupInterest> groupInterests;
	private ArrayList<GroupInterest> groupInterestsToDelete;
	private ArrayList<GroupPoi> groupPois;
	private ArrayList<GroupPoi> groupPoisToDelete;
	private ArrayList<GroupTour> groupTours;
	private ArrayList<GroupTour> groupToursToDelete;
	private ArrayList<User> members;
	private ArrayList<Suggestion> pendingSuggestions;

	public Group(Integer id, String name, String description, Integer type,
			String password, User owner, Integer ownerId,
			ArrayList<GeneralItem> groupInterests,
			ArrayList<GeneralItem> groupPois,
			ArrayList<GeneralItem> groupTours, boolean joined,
			ArrayList<User> members) {
		
		this(id, name, description, type, password, owner, ownerId,
				groupInterests, groupPois, groupTours, joined);
		this.members = members;
	}

	public Group(Integer id, String name, String description, Integer type,
			String password, User owner, Integer ownerId,
			ArrayList<GeneralItem> groupInterests,
			ArrayList<GeneralItem> groupPois,
			ArrayList<GeneralItem> groupTours, boolean joined) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.password = password;
		this.owner = owner;
		this.ownerId = ownerId;
		this.joined = joined;

		this.groupInterests = new ArrayList<GroupInterest>();
		this.groupInterests
				.addAll((Collection<? extends GroupInterest>) groupInterests);
		this.groupInterestsToDelete = new ArrayList<GroupInterest>();

		this.groupPois = new ArrayList<GroupPoi>();
		this.groupPois.addAll((Collection<? extends GroupPoi>) groupPois);
		this.groupPoisToDelete = new ArrayList<GroupPoi>();

		this.groupTours = new ArrayList<GroupTour>();
		this.groupTours.addAll((Collection<? extends GroupTour>) groupTours);
		this.groupToursToDelete = new ArrayList<GroupTour>();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ArrayList<User> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}

	public ArrayList<GroupPoi> getGroupPois() {
		return groupPois;
	}

	public void setGroupPois(ArrayList<GeneralItem> groupPois) {
		this.groupPois.clear();
		this.groupPois.addAll((Collection<? extends GroupPoi>) groupPois);
	}
	
	public void addGroupPoi(GroupPoi groupPoi){
		this.groupPois.add(groupPoi);
	}

	public ArrayList<GroupPoi> getGroupPoisToDelete() {
		return groupPoisToDelete;
	}

	public void setGroupPoisToDelete(ArrayList<GroupPoi> groupPoisToDelete) {
		this.groupPoisToDelete = groupPoisToDelete;
	}

	public ArrayList<GroupInterest> getGroupInterests() {
		return groupInterests;
	}

	public void setGroupInterests(ArrayList<GeneralItem> groupInterests) {
		this.groupInterests.clear();
		this.groupInterests
				.addAll((Collection<? extends GroupInterest>) groupInterests);
	}

	public ArrayList<GroupInterest> getGroupInterestsToDelete() {
		return groupInterestsToDelete;
	}

	public void setGroupInterestsToDelete(
			ArrayList<GroupInterest> groupInterestsToDelete) {
		this.groupInterestsToDelete = groupInterestsToDelete;
	}

	public ArrayList<GroupTour> getGroupTours() {
		return groupTours;
	}

	public void setGroupTours(ArrayList<GeneralItem> groupTours) {
		this.groupTours.clear();
		this.groupTours.addAll((Collection<? extends GroupTour>) groupTours);
	}	

	public ArrayList<GroupTour> getGroupToursToDelete() {
		return groupToursToDelete;
	}

	public void setGroupToursToDelete(ArrayList<GroupTour> groupToursToDelete) {
		this.groupToursToDelete = groupToursToDelete;
	}
	
	public void addGroupTour(GroupTour groupTour){
		this.groupTours.add(groupTour);
	}
	
	public Boolean isJoined() {
		return joined;
	}

	public void joined(Boolean joined) {
		this.joined = joined;
	}

	public ArrayList<Suggestion> getPendingSuggestions() {
		return pendingSuggestions;
	}

	public void setPendingSuggestions(ArrayList<Suggestion> pendingSuggestions) {
		this.pendingSuggestions = pendingSuggestions;
	}
	
	public void removeSuggestion(Suggestion suggestion) {
		this.pendingSuggestions.remove(suggestion);
	}

	public boolean isPrivate() {
		return type.equals(PRIVATE);
	}

	public boolean isPublic() {
		return type.equals(PUBLIC);
	}

	public boolean isOwner(User user) {
		return ownerId.equals(user.getId());
	}
	
	public void addMember(User member) {
		members.add(member);	
	}
	
	public void removeMember(User member) {
		members.remove(member);
	}
	
	public void findAndRemoveMember(User member) {
		User existingMember = findMember(member);
		removeMember(existingMember);		
	}
	
	public User findMember(User memberToFind) {
		User member = null; 
		for (User existingMember : members)
			if(existingMember.getId().equals(memberToFind.getId())) {
					member = existingMember;
					break;
			}
		return member;
	}

	public void updateGroupInterests(
			ArrayList<GroupInterest> oldGroupInterests,
			ArrayList<GeneralItem> newGroupInterests) {
		this.groupInterests.addAll(oldGroupInterests);

		ArrayList<GroupInterest> groupInterestsToAdd = new ArrayList<GroupInterest>();
		ArrayList<Integer> newSelectedInterestIds = new ArrayList<Integer>();
		ArrayList<Integer> oldSelectedInterestIds = new ArrayList<Integer>();

		// agregar los que no estaban y borrar los que no estan

		// vemos todos los nuevos seleccionados y armamos 1 array de Ids de
		// Interests
		for (int j = 0; j < newGroupInterests.size(); j++) {
			newSelectedInterestIds.add(((GroupInterest) newGroupInterests
					.get(j)).getInterestId());
		}

		// vemos cuales vamos a borrar(los que estaban antes y no vinieron
		// seleccionados ahora)
		for (int i = 0; i < getGroupInterests().size(); i++) {
			GroupInterest groupInterest = getGroupInterests().get(i);
			oldSelectedInterestIds.add(groupInterest.getInterestId());

			// si no esta entre los nuevos lo seteamos como borrado
			if (!newSelectedInterestIds.contains(groupInterest.getInterestId())) {
				getGroupInterests().get(i).setDeleted(true);
			}
		}

		// vemos cuales vamos a crear(los que vinieron seleccionados ahora y no
		// estaban antes)
		for (int i = 0; i < newGroupInterests.size(); i++) {
			GroupInterest groupInterest = (GroupInterest) newGroupInterests
					.get(i);
			groupInterest.setGroupId(this.getId());
			if (!oldSelectedInterestIds.contains(groupInterest.getInterestId())) {
				groupInterestsToAdd.add(groupInterest);
			}
		}

		this.groupInterests.addAll(groupInterestsToAdd);
	}

	public void updateGroupPois(ArrayList<GroupPoi> oldGroupPois,
			ArrayList<GeneralItem> newGroupPois) {
		this.groupPois.addAll(oldGroupPois);

		ArrayList<GroupPoi> groupPoisToAdd = new ArrayList<GroupPoi>();
		ArrayList<Integer> newSelectedPoiIds = new ArrayList<Integer>();
		ArrayList<Integer> oldSelectedPoiIds = new ArrayList<Integer>();

		// agregar los que no estaban y borrar los que no estan

		// vemos todos los nuevos seleccionados y armamos 1 array de GroupPois y
		// otro de Ids de Pois
		for (int j = 0; j < newGroupPois.size(); j++) {
			newSelectedPoiIds.add(((GroupPoi) newGroupPois.get(j)).getPoiId());
		}

		// vemos cuales vamos a borrar(los que estaban antes y no vinieron
		// seleccionados ahora)
		for (int i = 0; i < getGroupPois().size(); i++) {
			GroupPoi groupPoi = getGroupPois().get(i);
			oldSelectedPoiIds.add(groupPoi.getPoiId());

			// si no esta entre los nuevos lo seteamos como borrado
			if (!newSelectedPoiIds.contains(groupPoi.getPoiId())) {
				getGroupPois().get(i).setDeleted(true);
			}
		}

		// vemos cuales vamos a crear(los que vinieron seleccionados ahora y no
		// estaban antes)
		for (int i = 0; i < newGroupPois.size(); i++) {
			GroupPoi groupPoi = (GroupPoi) newGroupPois.get(i);
			groupPoi.setGroupId(this.getId());
			if (!oldSelectedPoiIds.contains(groupPoi.getPoiId())) {
				groupPoisToAdd.add(groupPoi);
			}
		}

		this.groupPois.addAll(groupPoisToAdd);
	}

	public void updateGroupTours(ArrayList<GroupTour> oldGroupTours,
			ArrayList<GeneralItem> newGroupTours) {
		this.groupTours.addAll(oldGroupTours);

		ArrayList<GroupTour> groupToursToAdd = new ArrayList<GroupTour>();
		ArrayList<Integer> newSelectedTourIds = new ArrayList<Integer>();
		ArrayList<Integer> oldSelectedTourIds = new ArrayList<Integer>();

		// agregar los que no estaban y borrar los que no estan

		// vemos todos los nuevos seleccionados y armamos 1 array de GroupTours
		// y otro de Ids de Tours
		for (int j = 0; j < newGroupTours.size(); j++) {
			newSelectedTourIds.add(((GroupTour) newGroupTours.get(j))
					.getTourId());
		}

		// vemos cuales vamos a borrar(los que estaban antes y no vinieron
		// seleccionados ahora)
		for (int i = 0; i < getGroupTours().size(); i++) {
			GroupTour groupTour = getGroupTours().get(i);
			oldSelectedTourIds.add(groupTour.getTourId());

			// si no esta entre los nuevos lo seteamos como borrado
			if (!newSelectedTourIds.contains(groupTour.getTourId())) {
				getGroupTours().get(i).setDeleted(true);
			}
		}

		// vemos cuales vamos a crear(los que vinieron seleccionados ahora y no
		// estaban antes)
		for (int i = 0; i < newGroupTours.size(); i++) {
			GroupTour groupTour = (GroupTour) newGroupTours.get(i);
			groupTour.setGroupId(this.getId());
			if (!oldSelectedTourIds.contains(groupTour.getTourId())) {
				groupToursToAdd.add(groupTour);
			}
		}

		this.groupTours.addAll(groupToursToAdd);
	}

	public static Group fromJSON(JSONObject groupJson) throws JSONException,
			ParseException {
		JSONArray groupsInterestsJson = groupJson
				.getJSONArray("groups_interests");
		JSONArray groupsPoisJson = groupJson.getJSONArray("groups_pois");
		JSONArray groupsToursJson = groupJson.getJSONArray("groups_tours");
		JSONArray groupMembersJson = groupJson.getJSONArray("users");

		ArrayList<GeneralItem> groupInterestsToAdd = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> groupPoisToAdd = new ArrayList<GeneralItem>();
		ArrayList<GeneralItem> groupToursToAdd = new ArrayList<GeneralItem>();
		ArrayList<User> groupMembers = new ArrayList<User>();

		for (int index = 0; index < groupsInterestsJson.length(); index++) {
			JSONObject groupsInterestJson = groupsInterestsJson
					.getJSONObject(index);
			GroupInterest groupInterest = GroupInterest
					.fromJSON(groupsInterestJson);
			groupInterestsToAdd.add(groupInterest);
		}

		for (int index = 0; index < groupsPoisJson.length(); index++) {
			JSONObject groupsPoiJson = groupsPoisJson.getJSONObject(index);
			GroupPoi groupPoi = GroupPoi.fromJSON(groupsPoiJson);
			groupPoisToAdd.add(groupPoi);
		}

		for (int index = 0; index < groupsToursJson.length(); index++) {
			JSONObject groupsTourJson = groupsToursJson.getJSONObject(index);
			GroupTour groupTour = GroupTour.fromJSON(groupsTourJson);
			groupToursToAdd.add(groupTour);
		}

		for (int index = 0; index < groupMembersJson.length(); index++) {
			JSONObject groupMemberJson = groupMembersJson.getJSONObject(index);
			User groupMember = User.fromJSON(groupMemberJson);
			groupMembers.add(groupMember);
		}

		Group group = new Group(groupJson.getInt("id"),
				groupJson.getString("name"),
				groupJson.getString("description"),
				groupJson.getInt("group_type"),
				groupJson.getString("password"), User.fromJSON(groupJson
						.getJSONObject("user")), groupJson.getInt("user_id"),
				groupInterestsToAdd, groupPoisToAdd, groupToursToAdd,
				groupJson.getBoolean("joined"), groupMembers);
		
		if (groupJson.has("pending_suggestions") && !groupJson.isNull("pending_suggestions")){
			ArrayList<Suggestion> suggestionsToAdd = new ArrayList<Suggestion>();
			JSONArray suggestionsJson = groupJson.getJSONArray("pending_suggestions");
			
			for (int i = 0; i < suggestionsJson.length(); i++) {
	    	    JSONObject suggestionJson = suggestionsJson.getJSONObject(i);    	        
	    	    Suggestion suggestion = Suggestion.fromJSON(suggestionJson);
	    	    suggestionsToAdd.add(suggestion);    	    
	    	}
			
			group.setPendingSuggestions(suggestionsToAdd);
		}

		return group;
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		try {
			if (getId() != null) {
				jsonObject.put("id", getId());
			}

			jsonObject.put("name", getName());
			jsonObject.put("description", getDescription());
			jsonObject.put("group_type", getType());
			jsonObject.put("password", getPassword());
			jsonObject.put("user_id", getOwnerId());

			JSONArray groupInterestsJson = new JSONArray();
			for (int i = 0; i < getGroupInterests().size(); i++) {
				GroupInterest groupInterest = getGroupInterests().get(i);
				groupInterestsJson.put(groupInterest.toJSON());
			}

			if (groupInterestsJson.length() > 0) {
				jsonObject.put("groups_interests_attributes",
						groupInterestsJson);
			}

			JSONArray groupPoisJson = new JSONArray();
			for (int i = 0; i < getGroupPois().size(); i++) {
				GroupPoi groupPoi = getGroupPois().get(i);
				groupPoisJson.put(groupPoi.toJSON());
			}

			if (groupPoisJson.length() > 0) {
				jsonObject.put("groups_pois_attributes", groupPoisJson);
			}

			JSONArray groupToursJson = new JSONArray();
			for (int i = 0; i < getGroupTours().size(); i++) {
				GroupTour groupTour = getGroupTours().get(i);
				groupToursJson.put(groupTour.toJSON());
			}

			if (groupToursJson.length() > 0) {
				jsonObject.put("groups_tours_attributes", groupToursJson);
			}

			JSONObject groupJson = new JSONObject();
			groupJson.put("group", jsonObject);
			return groupJson;
		} catch (JSONException e) {
			// TODO: handlear error
			e.printStackTrace();
			return jsonObject;
		}

	}
	
}