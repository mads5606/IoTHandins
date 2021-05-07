#include <stdio.h>
#include <string.h>
#include <FreeRTOS.h>
#include <task.h>
#include <bl_uart.h>
#include "bl_gpio.h"

#include <bl602_gpio.h>
#include <bl602_adc.h>
#include <bl_rtc.h>

#include "system.h"

#define ADC_CHANNEL (ADC_CHAN1)
#define ADC_PIN (4)

void TaskSample (void *pvParameters)
{
    ADC_CFG_Type cfg = {
        .v18Sel=ADC_V18_SEL_1P82V,          // ADC 1.8V select
        .v11Sel=ADC_V11_SEL_1P1V,           // ADC 1.1V select
        .clkDiv=ADC_CLK_DIV_16,             // Clock divider
        .gain1=ADC_PGA_GAIN_NONE,           // PGA gain 1
        .gain2=ADC_PGA_GAIN_NONE,           // PGA gain 2
        .chopMode=ADC_CHOP_MOD_AZ_PGA_ON,   // ADC chop mode select
        .biasSel=ADC_BIAS_SEL_MAIN_BANDGAP, // ADC current form main or aon bandgap
        .vcm=ADC_PGA_VCM_1V,                // ADC VCM value
        .vref=ADC_VREF_3P2V,                // ADC voltage reference
        .inputMode=ADC_INPUT_SINGLE_END,    // ADC input signal type
        .resWidth=ADC_DATA_WIDTH_12,        // ADC resolution and oversample rate
        .offsetCalibEn=0,                   // Offset calibration enable
        .offsetCalibVal=0,                  // Offset calibration value
    };
    ADC_FIFO_Cfg_Type fifo_cfg = {
        .fifoThreshold = ADC_FIFO_THRESHOLD_1, // ADC FIFO threshold
        .dmaEn = DISABLE,                      // ADC DMA enable
    };
    
    ADC_Reset();
    ADC_Disable();
    ADC_Enable();
    ADC_Init(&cfg);
    ADC_Channel_Config(ADC_CHANNEL, ADC_CHAN_GND, 0);
    ADC_FIFO_Cfg(&fifo_cfg);
    
    bl_rtc_init();
    
    while (1) {
        uint32_t start = bl_rtc_get_timestamp_ms();
        ADC_Start();

        while (ADC_Get_FIFO_Count() == 0)
            ;
        uint32_t regval = ADC_Read_FIFO();
        uint32_t t = bl_rtc_get_timestamp_ms();
        
        // from ADC_Parse_Result
        unsigned int val = (unsigned int)(((regval&0xffff)>>4)/1);

        printf("%u,%u\n", t, val);

        //Makes delay based on difference between start and end time
        uint32_t diff = bl_rtc_get_timestamp_ms() - start;
        delay(100-diff);
    }
}

void bfl_main(void)
{
    system_init();
    bl_uart_init(0, 16, 7, 255, 255, 9600);
    printf("booted\n");
    xTaskCreate(TaskSample, "Blink", 1024, NULL, 1, NULL);
    vTaskStartScheduler();
}