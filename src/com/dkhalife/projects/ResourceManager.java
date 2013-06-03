package com.dkhalife.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * This class manages the resources of the game
 * 
 * @author Dany Khalife
 * 
 */
public abstract class ResourceManager {
	// We need to store all images and sounds used by the game
	private static Map<String, Image> images = new HashMap<String, Image>();
	private static Map<String, Sound> sounds = new HashMap<String, Sound>();

	/**
	 * 
	 * This method loads resources from an XML file. The resources are then
	 * loaded in deferred processing.
	 * 
	 * @param path The path to the XML file
	 * @throws SlickException
	 * 
	 */
	public static void loadResources(String path) throws SlickException {
		try {
			// We'll set the processing to deferred
			LoadingList.setDeferredLoading(true);

			// We'll need to load the resources file into
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new FileInputStream(new File(path)));

			// We need to normalize the document element
			document.getDocumentElement().normalize();

			// Next we'll iterate over the resources found
			NodeList resources = document.getElementsByTagName("resource");

			for (int i = 0; i < resources.getLength(); i++) {
				Node n = resources.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE) {
					// We'll convert the node into an element
					Element resource = (Element) n;

					// We'll fetch the atribute of the resource
					String type = resource.getAttribute("type");

					// We'll load the resource into the appropriate category
					if (type.equals("image")) {
						addImage(resource);
					}
					else if (type.equals("sound")) {
						addSound(resource);
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new SlickException("Resources XML file could not be loaded");
		}

	}

	/**
	 * 
	 * Getter for an image resource
	 * 
	 * @param id The Id of the image
	 * @return The loaded image
	 * 
	 */
	public final static Image getImage(String id) {
		return images.get(id);
	}

	/**
	 * 
	 * This method adds an image resource
	 * 
	 * @param resource The resource to add as an image
	 * @throws SlickException
	 * 
	 */
	private final static void addImage(Element resource) throws SlickException {
		// We'll get the id and path for this image
		String id = resource.getAttribute("id");
		String fileName = resource.getTextContent();

		// We'll make sure we have a valid path
		if (fileName == null || fileName.length() == 0)
			throw new SlickException("Image resource [" + id + "] has an invalid path");

		// And we'll try to load it into our cache
		try {
			images.put(id, new Image("res/images/" + fileName));
		} catch (SlickException e) {
			throw new SlickException("Image resource [" + id + "] could not be loaded", e);
		}
	}

	/**
	 * 
	 * Getter for a sound resource
	 * 
	 * @param id The Id of the sound
	 * @return The loaded sound
	 * 
	 */
	public final static Sound getSound(String id) {
		//sounds.get(id).
		return sounds.get(id);
	}

	/**
	 * 
	 * This method adds a sound resource
	 * 
	 * @param resource The resource to add as a sound
	 * @throws SlickException
	 * 
	 */
	private final static void addSound(Element resource) throws SlickException {
		// We'll get the id and path for this sound
		String id = resource.getAttribute("id");
		String fileName = resource.getTextContent();

		// We'll make sure we have a valid path
		if (fileName == null || fileName.length() == 0)
			throw new SlickException("Sound resource [" + id + "] has an invalid path");

		// And we'll try to load it into our cache
		try {
			sounds.put(id, new Sound("res/sounds/" + fileName));
		} catch (SlickException e) {
			throw new SlickException("Sound resource [" + id + "] could not be loaded", e);
		}
	}
}
