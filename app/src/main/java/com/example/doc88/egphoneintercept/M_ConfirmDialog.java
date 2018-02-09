package com.example.doc88.egphoneintercept;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class M_ConfirmDialog extends Dialog {

	public M_ConfirmDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public M_ConfirmDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private int positiveButtonTextColor;
		private int negativeButtonTextColor;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 *
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 *
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 *
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
										 OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
										 OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
										 OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
										 OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		public Builder setNegativeButtonColor(int negativeButtonTextColor) {
			this.negativeButtonTextColor = negativeButtonTextColor;
			return this;
		}
		public Builder setPositiveButtonColor(int positiveButtonTextColor) {
			this.positiveButtonTextColor = positiveButtonTextColor;
			return this;
		}

		public M_ConfirmDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final M_ConfirmDialog dialog = new M_ConfirmDialog(context,
					R.style.my_dialog);
			View layout = inflater.inflate(R.layout.dialog_confirm, null);
			dialog.setContentView(layout);
			// set the cancel button
			if (positiveButtonText != null) {
				((TextView) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if(positiveButtonTextColor != 0){
					((TextView) layout.findViewById(R.id.positiveButton))
							.setTextColor(context.getResources().getColor(positiveButtonTextColor));
				}

				if (positiveButtonClickListener != null) {
					layout.findViewById(R.id.positiveButton)
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((TextView) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if(negativeButtonTextColor != 0){
					((TextView) layout.findViewById(R.id.negativeButton))
							.setTextColor(context.getResources().getColor(negativeButtonTextColor));
				}
				if (negativeButtonClickListener != null) {
					layout.findViewById(R.id.negativeButton)
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				TextView m_tv = (TextView) layout.findViewById(R.id.message);
				m_tv.setText(message);
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
