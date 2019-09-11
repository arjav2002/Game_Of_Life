package com.arjav.gameoflife.client.game.graphics;

public class Model {

	private Texture texture;
	private VertexArray vertArr;
	private Shader shader;
	private float[] vertices, texCoords;
	private byte[] indices;
	private String texturePath, vertexShaderPath, fragmentShaderPath;
	
	public Model(String vertexShaderPath, String fragmentShaderPath, String texturePath, float[] vertices, byte[] indices, float[] texCoords) {
		this.texturePath = texturePath;
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
		this.vertexShaderPath = vertexShaderPath;
		this.fragmentShaderPath = fragmentShaderPath;
	}
	
	public void init() {
		texture = new Texture(texturePath);
		vertArr = new VertexArray(vertices, indices, texCoords);
		shader = new Shader(vertexShaderPath, fragmentShaderPath);
	}
	
	public void render() {
		texture.bind();
		vertArr.bind();
		shader.enable();
		
		vertArr.draw();
		
		shader.disable();
		vertArr.unbind();
		texture.unbind();
	}
	
	public Shader getShader() {
		return shader;
	}
}
