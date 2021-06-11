package net.mysticcloud.spigot.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.mysticcloud.spigot.core.utils.CoreUtils;

public class MessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("mystic:mystic")) {
			return;
		}

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF() + "";
		String p = in.readUTF();
		if (subchannel.contains("kick")) {
			CoreUtils.proxyKick(p);
		}
	}
}
