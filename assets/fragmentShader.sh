precision mediump float; // tell GPU how much precision needed to process float ; highp mediump lowp
varying vec2 vTextureCoord;
uniform sampler2D sTexture;
void main(){
	gl_FragColor = texture2D(sTexture, vTextureCoord); 
}