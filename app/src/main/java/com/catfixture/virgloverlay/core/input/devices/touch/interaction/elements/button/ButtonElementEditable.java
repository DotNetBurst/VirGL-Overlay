package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.BType.BUTTON_TYPE_KEYBOARD;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.BType.BUTTON_TYPE_MOUSE;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.KeyCode;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.codes.MouseCode;
import com.catfixture.virgloverlay.core.input.codes.MouseCodes;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.CommonElementEditor;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;
import com.catfixture.virgloverlay.ui.common.genAdapter.DisplayType;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericSpinnerAdapter;
import com.catfixture.virgloverlay.ui.utils.Utils;

public class ButtonElementEditable extends CommonElementEditor {
    private TableLayout root; 

    public ButtonElementEditable(Context context, IInputWindowElement parentItem) {
        super(context, parentItem);

        InputTouchControlElementData data = (InputTouchControlElementData) parentItem.GetData();
        root = (TableLayout) View.inflate(context, R.layout.editable_button_element, null);

        //ICON
        Spinner icon = root.findViewById(R.id.icon);
        GenericSpinnerAdapter<Icon> gsa = new GenericSpinnerAdapter<>(context, R.layout.touch_controls_list_item_with_icon, Icon.icons, (i) -> {
        });
        gsa.EnableCustomItemAction((view, pos, displayType) -> {
            if ( pos >= Icon.icons.length) return;
            final int newIcon = gsa.getItem(pos).drawable;
            final ImageView iconView = view.findViewById(R.id.icon);
            final TextView tv = view.findViewById(R.id.text);
            if ( newIcon == -1) {
                iconView.setVisibility(GONE);
            } else {
                iconView.setVisibility(VISIBLE);
                iconView.setImageResource(newIcon);
            }
        });
        icon.setAdapter(gsa);
        icon.setOnItemSelectedListener(null);
        icon.setSelection(Icon.SpinnerIconPos(data.icon));
        icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if ( pos >= Icon.icons.length) return;
                final int newIcon = gsa.getItem(pos).drawable;
                if ( newIcon != data.icon) {
                    data.SetIcon(newIcon);
                    parentItem.Reinflate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        SetupButtonType(data);

        //TYPE
        Spinner buttonType = root.findViewById(R.id.buttonType);
        buttonType.setOnItemSelectedListener(null);
        ArrayAdapter<String> buttonTypesAdapter = Utils.InitSpinner(context, buttonType, 0, R.layout.touch_controls_list_item);
        buttonTypesAdapter.addAll(BType.types);
        buttonType.setAdapter(buttonTypesAdapter);
        buttonType.setSelection(data.buttonType);

        Utils.AttachSpinnerAction(buttonType, is -> {
            if ( data.buttonType != is) {
                data.SetButtonType(is);
                parentItem.Reinflate();
            }
        });

        //SHAPE
        Spinner buttonShape = root.findViewById(R.id.buttonShape);
        buttonShape.setOnItemSelectedListener(null);
        ArrayAdapter<String> buttonShapesAdapter = Utils.InitSpinner(context, buttonShape, 0, R.layout.touch_controls_list_item);
        buttonShapesAdapter.addAll(Shapes.shapes);
        buttonShape.setAdapter(buttonShapesAdapter);
        buttonShape.setSelection(data.buttonShape);

        Utils.AttachSpinnerAction(buttonShape, is -> {
            if ( data.buttonShape != is) {
                data.SetButtonShape(is);
                parentItem.Reinflate();
            }
        });

        TableLayout mainTab = super.root.findViewById(R.id.table);
        while (root.getChildCount() > 0) {
            View curr = root.getChildAt(0);
            root.removeView(curr);
            mainTab.addView(curr);
        }
    }

    private void SetupButtonType(InputTouchControlElementData data) {
        TableRow buttonCodeRow = root.findViewById(R.id.kbcodeRow);
        TableRow mouseCodeRow = root.findViewById(R.id.mscodeRow);

        buttonCodeRow.setVisibility(data.buttonType == BUTTON_TYPE_KEYBOARD ? VISIBLE : GONE);
        mouseCodeRow.setVisibility(data.buttonType == BUTTON_TYPE_MOUSE ? VISIBLE : GONE);

        switch (data.buttonType) {
            case BUTTON_TYPE_KEYBOARD: {
                //BUTTON CODE
                Spinner buttonCode = root.findViewById(R.id.buttonCode);
                buttonCode.setOnItemSelectedListener(null);
                final ArrayAdapter<KeyCode> buttonCodesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
                buttonCodesAdapter.addAll(KeyCodes.codes);
                buttonCode.setAdapter(buttonCodesAdapter);
                buttonCode.setSelection(KeyCodes.GetCodeIndex(data.keyCode));

                buttonCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                        final int newKeyCode = buttonCodesAdapter.getItem(is).code;
                        if (data.keyCode != newKeyCode) {
                            data.SetKeyCode(newKeyCode);
                            parentItem.Reinflate();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                break;
            }
            case BType.BUTTON_TYPE_MOUSE: {
                //MOUSE CODE
                Spinner mouseCode = root.findViewById(R.id.mouseCode);
                mouseCode.setOnItemSelectedListener(null);
                final ArrayAdapter<MouseCode> mouseCodesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
                mouseCodesAdapter.addAll(MouseCodes.codes);
                mouseCode.setAdapter(mouseCodesAdapter);
                mouseCode.setSelection(MouseCodes.GetCodeIndex(data.mouseCode));

                mouseCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                        final int newMouseCode = mouseCodesAdapter.getItem(is).code;
                        if (data.mouseCode != newMouseCode) {
                            data.SetMouseCode(newMouseCode);
                            parentItem.Reinflate();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                break;
            }
            case BType.BUTTON_TYPE_GAMEPAD: {
                break;
            }
            default:
                break;
        }
    }
}