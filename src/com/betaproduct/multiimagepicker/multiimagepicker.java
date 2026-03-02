package com.betaproduct.multiimagepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.List;

@DesignerComponent(version = 1,
    description = "Extension Multi Image Picker by BetaMedia",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "ai_images/extension.png")
@SimpleObject(external = true)
public class multiimagepicker extends AndroidNonvisibleComponent implements ActivityResultListener {

    private final Activity activity;
    private final int requestCode = 1024;

    public multiimagepicker(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Membuka galeri untuk memilih banyak gambar.")
    public void PickImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        form.registerForActivityResult(this);
        activity.startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), requestCode);
    }

    @Override
    public void resultReturned(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
            List<String> images = new ArrayList<>();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    images.add(imageUri.toString());
                }
            } else if (data.getData() != null) {
                images.add(data.getData().toString());
            }
            AfterPicking(YailList.makeList(images));
        }
    }

    @SimpleEvent(description = "Event setelah gambar dipilih.")
    public void AfterPicking(YailList imageList) {
        EventDispatcher.dispatchEvent(this, "AfterPicking", imageList);
    }
}
