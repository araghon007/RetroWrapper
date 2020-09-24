package com.zero.retrowrapper.installer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Installer
{
	
	//I know that this is horrible code and practices. It will work for awhile, but when it breaks this message will make me or you despair that I didn't do a better job.
	
	int versionCount;
	String workingDirectory;
	File directory;
	File[] directories;
	File versions;
	JButton install;
	JButton uninstall;
	
	JComboBox<String> list = new JComboBox<String>();
	MutableComboBoxModel<String> model = (MutableComboBoxModel<String>)list.getModel(); //a surprise to be used later
	
    String installerVersion = "1.4.4";
    String bonusText = "Skins & Java 7 work now!"; 
	
	public String defaultWorkingDirectory() {
		
		String OS = (System.getProperty("os.name")).toUpperCase();
		
		String defaultWorkingDirectory;
		
		if (OS.contains("WIN")) //windows uses the %appdata%/.minecraft structure
		{
			defaultWorkingDirectory = System.getenv("AppData") + File.separator + ".minecraft";
		}
		else if (OS.contains("MAC")) //mac os uses %user%/Library/Library/Application Support/minecraft
		{
			defaultWorkingDirectory = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "minecraft";
		}else 
		{
			defaultWorkingDirectory = System.getProperty("user.home") + File.separator + ".minecraft";
		} 
		
		return defaultWorkingDirectory;

	}
	
	void refreshList(String givenDirectory) {
		
		versionCount = 0;
		list.removeAllItems();
		
		if (!givenDirectory.isEmpty()) {
			directory = new File(givenDirectory);
			
			directory.mkdirs();
		
			directories = null;
			versions = new File(directory, "versions");
		
			if(versions.exists())
			{
				directories = versions.listFiles();
			}
			
			if(!(directories == null || directories.length == 0)) {
		
			Arrays.sort(directories);
		
			//add items
		
			for(File f : directories)
			{
				if(f.isDirectory())
			{
				versionCount++;
				File json = new File(f, f.getName() + ".json");
				File jar = new File(f, f.getName() + ".jar");
				if(json.exists() && jar.exists() && !f.getName().contains("-wrapped") && !new File(versions, f.getName()+"-wrapped").exists())
				{
					try(Scanner s = new Scanner(json).useDelimiter("\\A"))
					{
						String content = s.next();
						if (content.contains("old_") && !content.contains("retrowrapper")) {
							model.addElement(f.getName());
						}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		
		}
		}
		
		//button visibility
		
		if (givenDirectory.isEmpty()) {
		
			install.setEnabled(false);
			uninstall.setEnabled(false);
			
		JOptionPane.showMessageDialog(null, "No directory / minecraft directory detected!\n", "Error", JOptionPane.INFORMATION_MESSAGE);
		
		} else if(!versions.exists())
		{
			install.setEnabled(false);
			uninstall.setEnabled(false);
			
			JOptionPane.showMessageDialog(null, "No Minecraft versions folder found!", "Error", JOptionPane.INFORMATION_MESSAGE);
			
		} else if (versionCount == 0) {
			
			install.setEnabled(false);
			uninstall.setEnabled(true);
			
			JOptionPane.showMessageDialog(null, "No wrappable versions found!", "Error", JOptionPane.INFORMATION_MESSAGE);
			
		} else if (list.getItemCount() == 0) {
			
			install.setEnabled(false);
			uninstall.setEnabled(true);
			
			JOptionPane.showMessageDialog(null, "All detected versions have already been wrapped!", "Info", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			
			install.setEnabled(true);
			uninstall.setEnabled(true);
			
		}
		
	}
	
	public Installer() throws Exception
	{
		
		workingDirectory = defaultWorkingDirectory();
		
		//final File[] directoriesFinal = directories;
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame("Retrowrapper - NeRd Fork");
		frame.setPreferredSize(new Dimension(654, 310));
		frame.setLayout(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false); //add resizing later
		
		JLabel label = new JLabel("Retrowrapper Installer");
		label.setFont(label.getFont().deriveFont(20f).deriveFont(Font.BOLD));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 10, 654, 40);
		frame.add(label);
		
		JLabel label3 = new JLabel(installerVersion + " - " + bonusText);
		label3.setFont(label.getFont().deriveFont(12f));
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setBounds(0, 30, 654, 40);
		frame.add(label3);

		JLabel label2 = new JLabel("\u00a92018 000");
		label2.setFont(label2.getFont().deriveFont(12f));
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setBounds(0, 250, 654, 20);
		frame.add(label2);
		
		list.setBounds(654/2 - 150, 100, 300, 30);
		
		frame.add(list);
		
		uninstall = new JButton("Uninstall ALL versions"); //uninstaller code
		uninstall.setBounds(654/2 - 100, 202, 200, 30);
		uninstall.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	for(File f : directories)
				{
					if(f.isDirectory() && f.getName().contains("-wrapped"))
					{
						deleteDirectory(f);
					}
				}
		    	
		    	File libDir = new File(directory, "libraries"+File.separator+"com"+File.separator+"zero");
		    	if(libDir.exists())
		    	{
		    		deleteDirectory(libDir);
		    	}

		    	JOptionPane.showMessageDialog(null, "Successfully uninstalled wrapper", "Success", JOptionPane.INFORMATION_MESSAGE);
		    	refreshList(workingDirectory);
		    	//System.exit(0);
		    }

			private void deleteDirectory(File f)
			{
				try
				{
					Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>()
					{
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
						{
							Files.delete(file);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
						{
							Files.delete(dir);
							return FileVisitResult.CONTINUE;
						}
					});
				} catch (IOException ee)
				{
					ee.printStackTrace();
				}				
			}
			
		});
		
		frame.add(uninstall);

		install = new JButton("Install"); //installation code
		install.setBounds(654/2 - 100, 160, 200, 40);
		
		install.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	String version = (String)list.getSelectedItem();
		    	
		    	try(Scanner s = new Scanner(new File(versions, version+File.separator+version+".json")).useDelimiter("\\A"))
		    	{
		    		String json = s.next();
		    		String versionWrapped = version+"-wrapped";

		    		json = json.replace("\"libraries\": [", "\"libraries\": [{\"name\": \"com.zero:retrowrapper:installer\"},");
		    		json = json.replace("\"libraries\":[", "\"libraries\":[{\"name\": \"com.zero:retrowrapper:installer\"},");
		    		json = json.replace("\""+version+"\"", "\""+versionWrapped+"\"");
		    		if(json.contains("VanillaTweaker"))
		    		{
		    			json = json.replace("net.minecraft.launchwrapper.AlphaVanillaTweaker", "com.zero.retrowrapper.RetroTweaker");
		    			json = json.replace("net.minecraft.launchwrapper.IndevVanillaTweaker", "com.zero.retrowrapper.RetroTweaker");
		    		}else
		    		{
		    			json = json.replace("--assetsDir ${game_assets}", "--assetsDir ${game_assets} --tweakClass com.zero.retrowrapper.RetroTweaker");
		    		}
		    		
		    		File wrapDir = new File(versions, versionWrapped+File.separator);
		    		wrapDir.mkdirs();
		    		
		    		File libDir = new File(directory, "libraries"+File.separator+"com"+File.separator+"zero"+File.separator+"retrowrapper"+File.separator+"installer");
		    		libDir.mkdirs();
		    		try(FileOutputStream fos = new FileOutputStream(new File(wrapDir, versionWrapped+".json")))
		    		{
			    		Files.copy(new File(versions, version+File.separator+version+".jar").toPath(), new File(wrapDir, versionWrapped+".jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
		    			fos.write(json.getBytes());
		    			fos.close();
			    		File jar = new File(URLDecoder.decode(Installer.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));			    		
			    		Files.copy(jar.toPath(), new File(libDir, "retrowrapper-installer.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
		    		} catch (IOException ee)
		    		{
						ee.printStackTrace();
					}	
		    	} catch (FileNotFoundException ee)
		    	{
					ee.printStackTrace();
				}		    	
		    	
		    	JOptionPane.showMessageDialog(null, "Successfully wrapped version\n"+version, "Success", JOptionPane.INFORMATION_MESSAGE);
		    	refreshList(workingDirectory);
		    	//System.exit(0);
		    }
		});
		
		frame.add(install);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		refreshList(workingDirectory);
	}
	
	public static void main(String[] args)
	{
		try
		{
			new Installer();
		}catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exception occured!\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
