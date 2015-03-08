package gopheratl.biolock.client;

import gopheratl.biolock.common.BioLock;
import gopheratl.biolock.common.TileEntityKeypadLock;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityRendererKeypad extends TileEntitySpecialRenderer {

	static float texPixel=1.0f/16f;
	
	public static class ButtonRenderer {
		public float x, y, z;
		public float w, h;
		public String label;
		
		public ButtonRenderer(float x, float y, float z, float w, float h, String label)
		{
			this.x=x*texPixel;
			this.y=y*texPixel;
			this.z=z*texPixel;
			this.w=w*texPixel;
			this.h=h*texPixel;
			this.label=label;
		}
		
		public void renderGeometry(Tessellator tessellator, float depth)
		{
			float tx=texPixel*2,ty=texPixel*2;
			
			tessellator.setNormal(0f,0f,-1f);
			tessellator.addVertexWithUV(x,   y,   z+depth, tx, ty);
			tessellator.addVertexWithUV(x,   y+h, z+depth, tx, ty+2f*texPixel);
			tessellator.addVertexWithUV(x+w, y+h, z+depth, tx+2f*texPixel, ty+2f*texPixel);
			tessellator.addVertexWithUV(x+w, y,   z+depth, tx+2f*texPixel, ty);
			
			tessellator.setNormal(-1f,0f,0f);
			
			tessellator.addVertexWithUV(x,   y,   z+depth, tx, ty);
			tessellator.addVertexWithUV(x,   y,   z+2*texPixel+depth, tx+2f*texPixel, ty);
			tessellator.addVertexWithUV(x,   y+h, z+2*texPixel+depth, tx+2f*texPixel, ty+2f*texPixel);
			tessellator.addVertexWithUV(x,   y+h, z+depth, tx, ty+2f*texPixel);
			
			tessellator.setNormal(1f,0f,0f);
			tessellator.addVertexWithUV(x+w,   y,   z+2*texPixel+depth, tx+2f*texPixel, ty);
			tessellator.addVertexWithUV(x+w,   y,   z+depth, tx, ty);
			tessellator.addVertexWithUV(x+w,   y+h, z+depth, tx, ty+2f*texPixel);
			tessellator.addVertexWithUV(x+w,   y+h, z+2*texPixel+depth, tx+2f*texPixel, ty+2f*texPixel);
			
			tessellator.setNormal(0f,-1f,0f);
			tessellator.addVertexWithUV(x,     y,   z+depth, tx, ty);
			tessellator.addVertexWithUV(x+w,   y,   z+depth, tx+2f*texPixel, ty);
			tessellator.addVertexWithUV(x+w,   y,   z+2*texPixel+depth, tx+2f*texPixel, ty+2f*texPixel);
			tessellator.addVertexWithUV(x,     y,   z+2*texPixel+depth, tx, ty+2f*texPixel);

			tessellator.setNormal(0f,1f,0f);
			tessellator.addVertexWithUV(x,     y+h,   z+2*texPixel+depth, tx+2f*texPixel, ty);
			tessellator.addVertexWithUV(x+w,   y+h,   z+2*texPixel+depth, tx+2f*texPixel, ty+2f*texPixel);
			tessellator.addVertexWithUV(x+w,   y+h,   z+depth, tx, ty+2f*texPixel);
			tessellator.addVertexWithUV(x,     y+h,   z+depth, tx, ty);
		}
		
		public void writeLabel(FontRenderer font, float depth)
		{
			/**/
			
			GL11.glPushMatrix();
			
			GL11.glTranslatef(x+w/2f, y+h/2f, depth+texPixel*-.07f);
			float scale=h/10;
			GL11.glScalef(-scale,-scale,scale);
			GL11.glTranslatef(.5f,.5f,0f);
			GL11.glDepthMask(false);
			int labelW=font.getStringWidth(label);
			font.drawString(label, -labelW/2, -4, 0);
			GL11.glDepthMask(true);
			
			GL11.glPopMatrix();
		}
	}
	
	ButtonRenderer buttons[];
	
	public TileEntityRendererKeypad()
	{
		super();
		buttons=new ButtonRenderer[12];
		
		buttons[0] =new ButtonRenderer(10f, 11.5f, 0f, 2f, 2f, "1");
		buttons[1] =new ButtonRenderer( 7f, 11.5f, 0f, 2f, 2f, "2");
		buttons[2] =new ButtonRenderer( 4f, 11.5f, 0f, 2f, 2f, "3");

		buttons[3] =new ButtonRenderer(10f,  8.5f, 0f, 2f, 2f, "4");
		buttons[4] =new ButtonRenderer( 7f,  8.5f, 0f, 2f, 2f, "5");
		buttons[5] =new ButtonRenderer( 4f,  8.5f, 0f, 2f, 2f, "6");

		buttons[6] =new ButtonRenderer(10f,  5.5f, 0f, 2f, 2f, "7");
		buttons[7] =new ButtonRenderer( 7f,  5.5f, 0f, 2f, 2f, "8");
		buttons[8] =new ButtonRenderer( 4f,  5.5f, 0f, 2f, 2f, "9");

		buttons[9] =new ButtonRenderer(10f,  2.5f, 0f, 2f, 2f, "*");
		buttons[10]=new ButtonRenderer( 7f,  2.5f, 0f, 2f, 2f, "0");
		buttons[11]=new ButtonRenderer( 4f,  2.5f, 0f, 2f, 2f, "#");
}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) 
	{
		TileEntityKeypadLock te=(TileEntityKeypadLock)tileEntity;
		Tessellator tessellator=Tessellator.instance;
		
		int bx=te.xCoord, by=te.yCoord, bz=te.zCoord;
		
		World world=te.getWorldObj();
		
		
		float brightness=BioLock.Blocks.keypadLock.getLightValue(world, bx, by, bz);
		int light=world.getLightBrightnessForSkyBlocks(bx,by,bz,0);				
		
		tessellator.setColorOpaque_F(brightness,brightness,brightness);		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(light&0xffff),(float)(light>>16));
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y,(float)z);
		GL11.glTranslatef(.5f,0,.5f);
		GL11.glRotatef(te.getAngle(),0f,1f,0f);
		GL11.glTranslatef(-.5f,0,-.5f);
		

		this.bindTexture(new ResourceLocation("BioLock:biolock_side"));
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0f, 0f, 1f);
		//inset face
		tessellator.addVertexWithUV( texPixel,    texPixel,    texPixel, texPixel,    1f-texPixel);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, texPixel, texPixel,    texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, texPixel, 1f-texPixel, texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    texPixel, 1f-texPixel, 1f-texPixel);

		
		//bottom lip front
		tessellator.addVertexWithUV( 0f,          0f,          0.001f,  0f,          0f);
		tessellator.addVertexWithUV( texPixel,    texPixel,    0.001f,  texPixel,    texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    0.001f,  1f-texPixel, texPixel);
		tessellator.addVertexWithUV( 1f,          0f,          0.001f,  1f,          0f);
		//top lip front
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, 0.001f,  texPixel,    1f-texPixel);
		tessellator.addVertexWithUV( 0f,          1f,          0.001f,  0f,          1f);
		tessellator.addVertexWithUV( 1f,          1f,          0.001f,  1f,          1f);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, 0.001f,   1f-texPixel, 1f-texPixel);
		//right lip front
		tessellator.addVertexWithUV( 0f,          0f,          0.001f,  0f,          0f);
		tessellator.addVertexWithUV( 0f,          1f,          0.001f,  0f,          1f);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, 0.001f,  texPixel,    1f-texPixel);
		tessellator.addVertexWithUV( texPixel,    texPixel,    0.001f,  texPixel,    texPixel);
		//left lip front
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    0.001f,  1f-texPixel, texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, 0.001f,  1f-texPixel, 1f-texPixel);
		tessellator.addVertexWithUV( 1f,          1f,          0.001f,  1f,          1f);
		tessellator.addVertexWithUV( 1f,          0f,          0.001f,  1f,          0f);

		//bottom lip inside
		tessellator.setNormal(0f,1f,0f);
		tessellator.addVertexWithUV( texPixel,    texPixel,    0f,       texPixel,    1f);
		tessellator.addVertexWithUV( texPixel,    texPixel,    texPixel, texPixel,    1f-texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    texPixel, 1f-texPixel, 1f-texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    0f,       1f-texPixel, 1f);
		//top lip inside
		tessellator.setNormal(0f,-1f,0f);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, texPixel, texPixel,    texPixel);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, 0f,       texPixel,    0f);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, 0f,       1f-texPixel, 0f);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, texPixel, 1f-texPixel, texPixel);
		//right lip inside
		tessellator.setNormal(1f,0f,0f);
		tessellator.addVertexWithUV( texPixel,    texPixel,    0f,       1f-texPixel, texPixel);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, 0f,       1f-texPixel, 1f-texPixel);
		tessellator.addVertexWithUV( texPixel,    1f-texPixel, texPixel, 1f,          1f-texPixel);
		tessellator.addVertexWithUV( texPixel,    texPixel,    texPixel, 1f,          texPixel);
		//left lip inside
		tessellator.setNormal(-1f,1f,0f);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    texPixel, 1f,          texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, texPixel, 1f,          1f-texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, 1f-texPixel, 0f,       1f-texPixel, 1f-texPixel);
		tessellator.addVertexWithUV( 1f-texPixel, texPixel,    0f,       1f-texPixel, texPixel);
		
		long time=world.getTotalWorldTime();
		
		for (int i=0; i<12; ++i)
			buttons[i].renderGeometry(tessellator,te.buttonStates[i].isPressed(time)?texPixel*.75f:0f);
		
		tessellator.draw();		
		
		FontRenderer font=this.func_147498_b();
		for (int i=0; i<12; ++i)
			buttons[i].writeLabel(font,te.buttonStates[i].isPressed(time)?texPixel*.75f:0f);
				
		
		GL11.glPopMatrix();
	}

}
