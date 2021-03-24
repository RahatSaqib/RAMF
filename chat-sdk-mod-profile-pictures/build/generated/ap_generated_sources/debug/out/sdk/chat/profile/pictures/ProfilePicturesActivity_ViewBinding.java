// Generated code from Butter Knife. Do not modify!
package sdk.chat.profile.pictures;

import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProfilePicturesActivity_ViewBinding implements Unbinder {
  private ProfilePicturesActivity target;

  @UiThread
  public ProfilePicturesActivity_ViewBinding(ProfilePicturesActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProfilePicturesActivity_ViewBinding(ProfilePicturesActivity target, View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
    target.gridLayout = Utils.findRequiredViewAsType(source, R.id.gridLayout, "field 'gridLayout'", GridLayout.class);
    target.root = Utils.findRequiredViewAsType(source, R.id.root, "field 'root'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfilePicturesActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.gridLayout = null;
    target.root = null;
  }
}
