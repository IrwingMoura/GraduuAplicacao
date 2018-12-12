package graduuaplicacao.graduuaplicacao.Model;

public class CardsModel {

    private int mImageSource;
    private String mText1;
    private String mText2;

    public CardsModel(int mImageSource, String mText1, String mText2) {
        this.mImageSource = mImageSource;
        this.mText1 = mText1;
        this.mText2 = mText2;
    }

    public void changeText1(String text) {
        mText1 = text;
    }

    public int getmImageSource() {
        return mImageSource;
    }

    public void setmImageSource(int mImageSource) {
        this.mImageSource = mImageSource;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
