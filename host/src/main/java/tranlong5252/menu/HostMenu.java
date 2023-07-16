package tranlong5252.menu;

import tranlong5252.RestaurantsManager;

public abstract class HostMenu {

	protected final RestaurantsManager main = RestaurantsManager.getInstance();

	public abstract void showMenu();
}
