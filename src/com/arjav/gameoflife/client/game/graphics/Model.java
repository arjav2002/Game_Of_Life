package com.arjav.gameoflife.client.game.graphics;

public class Model {

	private Texture texture;
	private VertexArray vertArr;
	private float[] vertices, texCoords;
	private byte[] indices;
	private String texturePath;
		
	public Model(String texturePath, float[] vertices, byte[] indices, float[] texCoords) {
		this.texturePath = texturePath;
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
	}
	
	public void init() {
		texture = new Texture(texturePath);
		vertArr = new VertexArray(vertices, indices, texCoords);
	}
	
	public void render() {
		texture.bind();
		vertArr.bind();
		
		vertArr.draw();
		
		vertArr.unbind();
		texture.unbind();
	}
	
	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
