package telran.ashkelon2020.accounting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "email" })
@Document(collection = "users")
public class UserAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5537379762348466179L;
	@Id
	String email;
	String name;
	String password;
	String avatar;
	String phone;
	//boolean isBlocked;
	List<String> roles = new ArrayList<>();
//	Set<String> favorites = new HashSet<>();
//	Set<String> activity = new HashSet<>();
	
	public boolean addRole(String role) {
		return roles.add(role);
	}

	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
//	public void addFavoritePost(String id) {
//		favorites.add(id);
//	}
//	
//	public void removeFavoritePost(String id) {
//		favorites.remove(id);
//	}
//	
//	public void addUserPost(String id) {
//		activity.add(id);
//	}
//	
//	public void removeUserPost(String id) {
//		activity.remove(id);
//	}

}
