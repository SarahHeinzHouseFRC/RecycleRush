namespace NetConsole_GUI
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.txt_Received = new System.Windows.Forms.RichTextBox();
            this.txt_SendText = new System.Windows.Forms.TextBox();
            this.btn_SendCommand = new System.Windows.Forms.Button();
            this.mainFlowPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.secondaryFlowPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.mainFlowPanel.SuspendLayout();
            this.secondaryFlowPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // txt_Received
            // 
            this.txt_Received.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.txt_Received.BackColor = System.Drawing.Color.Black;
            this.txt_Received.ForeColor = System.Drawing.Color.Lime;
            this.txt_Received.Location = new System.Drawing.Point(3, 3);
            this.txt_Received.Name = "txt_Received";
            this.txt_Received.ReadOnly = true;
            this.txt_Received.Size = new System.Drawing.Size(628, 450);
            this.txt_Received.TabIndex = 0;
            this.txt_Received.Text = "";
            // 
            // txt_SendText
            // 
            this.txt_SendText.Anchor = System.Windows.Forms.AnchorStyles.Left;
            this.txt_SendText.BackColor = System.Drawing.Color.Black;
            this.txt_SendText.ForeColor = System.Drawing.Color.Lime;
            this.txt_SendText.Location = new System.Drawing.Point(3, 8);
            this.txt_SendText.Name = "txt_SendText";
            this.txt_SendText.Size = new System.Drawing.Size(535, 20);
            this.txt_SendText.TabIndex = 1;
            // 
            // btn_SendCommand
            // 
            this.btn_SendCommand.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.btn_SendCommand.Location = new System.Drawing.Point(544, 3);
            this.btn_SendCommand.Name = "btn_SendCommand";
            this.btn_SendCommand.Size = new System.Drawing.Size(83, 30);
            this.btn_SendCommand.TabIndex = 2;
            this.btn_SendCommand.Text = "Send";
            this.btn_SendCommand.UseVisualStyleBackColor = true;
            this.btn_SendCommand.Click += new System.EventHandler(this.btn_SendCommand_Click);
            // 
            // mainFlowPanel
            // 
            this.mainFlowPanel.Controls.Add(this.txt_Received);
            this.mainFlowPanel.Controls.Add(this.secondaryFlowPanel);
            this.mainFlowPanel.FlowDirection = System.Windows.Forms.FlowDirection.TopDown;
            this.mainFlowPanel.Location = new System.Drawing.Point(0, 0);
            this.mainFlowPanel.Name = "mainFlowPanel";
            this.mainFlowPanel.Size = new System.Drawing.Size(637, 508);
            this.mainFlowPanel.TabIndex = 3;
            // 
            // secondaryFlowPanel
            // 
            this.secondaryFlowPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.secondaryFlowPanel.Controls.Add(this.txt_SendText);
            this.secondaryFlowPanel.Controls.Add(this.btn_SendCommand);
            this.secondaryFlowPanel.Location = new System.Drawing.Point(3, 459);
            this.secondaryFlowPanel.Name = "secondaryFlowPanel";
            this.secondaryFlowPanel.Size = new System.Drawing.Size(634, 33);
            this.secondaryFlowPanel.TabIndex = 4;
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.AppWorkspace;
            this.ClientSize = new System.Drawing.Size(634, 497);
            this.Controls.Add(this.mainFlowPanel);
            this.Name = "MainForm";
            this.Text = "SHARPConsole";
            this.Resize += new System.EventHandler(this.MainForm_Resize);
            this.mainFlowPanel.ResumeLayout(false);
            this.secondaryFlowPanel.ResumeLayout(false);
            this.secondaryFlowPanel.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.RichTextBox txt_Received;
        private System.Windows.Forms.TextBox txt_SendText;
        private System.Windows.Forms.Button btn_SendCommand;
        private System.Windows.Forms.FlowLayoutPanel mainFlowPanel;
        private System.Windows.Forms.FlowLayoutPanel secondaryFlowPanel;
    }
}

