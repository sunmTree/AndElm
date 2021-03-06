package elm.wx.com.andelm;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import elm.wx.com.andelm.util.SimpleTextWatcher;
import elm.wx.core.CompoundUpdate;
import elm.wx.core.Elm;
import elm.wx.com.core.reactive.mutable.Elms;
import elm.wx.com.core.reactive.mutable.Bind;
import elm.wx.core.MutableUpdate;
import elm.wx.com.core.reactive.mutable.ReactiveViewFactory;

/**
 * Created by wangwei on 2018/12/13.
 */
public class Forms implements Demo {

    static final String OK = "OK";

    ///////////////////////////////////////////////////////////////////////////
    // main
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public View main(Context context) {
        final View root = View.inflate(context, R.layout.elm_forms, null);
        Elms.construct(context, initUpdate(), new Form(), initView(root));
        return root;
    }

    ///////////////////////////////////////////////////////////////////////////
    // model
    ///////////////////////////////////////////////////////////////////////////

    static class Form {

        String name = "";
        String password = "";
        String passwordAgain = "";
        String status = OK;
    }

    ///////////////////////////////////////////////////////////////////////////
    // update
    ///////////////////////////////////////////////////////////////////////////

    private static final String KEY_NAME = "NAME";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_AGAIN = "AGAIN";

    @NonNull
    private Elm.Update<Form> initUpdate() {
        final CompoundUpdate<Form> update = new CompoundUpdate<>();
        update.add(KEY_NAME, (MutableUpdate<Form>) (action, param, model) -> model.name = ((String) param));
        update.add(KEY_PASSWORD, (MutableUpdate<Form>) (action, param, model) -> model.password = ((String) param));
        update.add(KEY_AGAIN, (MutableUpdate<Form>) (action, param, model) -> {
            model.passwordAgain = ((String) param);
            model.status = model.password.equals(model.passwordAgain) ? OK : "PASSWORD DO NOT MATCH";
        });
        return update;
    }

    ///////////////////////////////////////////////////////////////////////////
    // view
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    private ReactiveViewFactory<Form> initView(View root) {
        return (context, elm, bind) -> {
            EditText name = root.findViewById(R.id.input_name);
            name.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    elm.send(KEY_NAME, s.toString());
                }
            });

            EditText password = root.findViewById(R.id.input_password);
            password.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    elm.send(KEY_PASSWORD, s.toString());
                }
            });

            EditText again = root.findViewById(R.id.input_password_again);
            again.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    elm.send(KEY_AGAIN, s.toString());
                }
            });

            final Bind<Form> $ = new Bind<>(bind);

            TextView status = root.findViewById(R.id.text);
            $.text(status, form -> form.status);
            $.textColor(status, form -> OK.equalsIgnoreCase(form.status) ? Color.BLUE : Color.RED);

            return root;
        };
    }
}
