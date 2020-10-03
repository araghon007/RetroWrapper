package com.zero.retrowrapper.emulator.registry.handlers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;
import com.zero.retrowrapper.util.Base64;

public class SkinHandler extends EmulatorHandler implements IHandler
{
	private HashMap<String, byte[]> skinsCache = new HashMap<>();

	public SkinHandler(String url)
	{
		super(url);
	}

	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		String username = get.replace(url, "").replace(".png", "");

		if(skinsCache.containsKey(username))
		{
			os.write(skinsCache.get(username));
		}else
		{
			byte[] bytes3 = downloadSkin(username);
			if(bytes3 != null)
			{
				BufferedImage imgSkinRaw = ImageIO.read(new ByteArrayInputStream(bytes3));
				BufferedImage imgSkinFixed = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);

				imgSkinFixed.getGraphics().drawImage(imgSkinRaw, 0, 0, null);

				ByteArrayOutputStream osSkin = new ByteArrayOutputStream();

				ImageIO.write(imgSkinFixed, "png", osSkin);
				osSkin.flush();

				byte[] bytes = osSkin.toByteArray();
				os.write(bytes);

				skinsCache.put(username, bytes);
			}
		}
	}

	private byte[] downloadSkin(String username) throws IOException
	{
		//using json now

		File skinCache = new File(RetroEmulator.getInstance().getCacheDirectory(), username + ".png");

		try(InputStreamReader reader = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/"+username+"?at="+System.currentTimeMillis()).openStream()))
		{
			JsonObject profile1 = (JsonObject) Json.parse(reader);
			String uuid = profile1.get("id").asString();
			System.out.println(uuid);
			try(InputStreamReader reader2 = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid).openStream()))
			{
				JsonObject profile2 = (JsonObject) Json.parse(reader2);
				JsonArray properties = (JsonArray) profile2.get("properties");
				String base64 = "";
				for(JsonValue property : properties) {
					JsonObject propertyj = property.asObject();
					if(propertyj.get("name").asString().equalsIgnoreCase("textures"))
						base64 = propertyj.get("value").asString();
				}
				JsonObject textures1 = (JsonObject) Json.parse(new String(Base64.decode(base64)));
				JsonObject textures = (JsonObject) textures1.get("textures");
				JsonObject skin = (JsonObject) textures.get("SKIN");
				String skinURL = skin.get("url").asString();

				System.out.println(skinURL);
				InputStream is = new URL(skinURL).openStream();

				byte[] skinBytes = ByteUtils.readFully(is);

				try(FileOutputStream fos = new FileOutputStream(skinCache))
				{
					fos.write(skinBytes);
					fos.close();
				}

				return skinBytes;
			}
		}catch(Exception e)
		{
			e.printStackTrace();

			if(skinCache.exists())
			{
				try(FileInputStream fis = new FileInputStream(skinCache))
				{
					return ByteUtils.readFully(fis);
				}
			}

			return null;
		}
	}
}
