package com.arjav.gameoflife.client.game.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.arjav.gameoflife.client.glutils.MyBufferUtils;

public class VertexArray {
	
	private int vao, vbo, ibo, tbo;
	private int count;
	
	// attribute numbers in the vertex array
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	public VertexArray(int count) {
		this.count = count;
		vao = glGenVertexArrays();
	}
	
	public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
		count = indices.length;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, MyBufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW); // data is to uploaded once only
		glVertexAttribPointer(VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(VERTEX_ATTRIB);
		
		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, MyBufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(TCOORD_ATTRIB);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, MyBufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void bind() {
		glBindVertexArray(vao);
		if (ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void unbind() {
		if (ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public void draw() {
		if (ibo > 0) // if not null
			glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
		else
			glDrawArrays(GL_TRIANGLES, 0, count);
	}
	
	public void render() {
		bind();
		draw();
	}

}