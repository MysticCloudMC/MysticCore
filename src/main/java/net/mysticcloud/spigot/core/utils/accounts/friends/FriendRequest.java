package net.mysticcloud.spigot.core.utils.accounts.friends;

public class FriendRequest {

	FriendRequestStatus status = FriendRequestStatus.NONE;
	
	public void setStatus(FriendRequestStatus status) {
		this.status = status;
	}
	public FriendRequestStatus getStatus() {
		return status;
	}

}
