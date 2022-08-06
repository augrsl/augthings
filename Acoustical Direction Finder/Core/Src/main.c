/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

#define ADC_BUF_LEN 4096*3
#define ADC_DATA_LEN ADC_BUF_LEN/3
#define ADC_THOLD 175
#define CHARBUFF_LEN 20

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
ADC_HandleTypeDef hadc1;
DMA_HandleTypeDef hdma_adc1;

UART_HandleTypeDef huart2;
DMA_HandleTypeDef hdma_usart2_tx;

/* USER CODE BEGIN PV */

uint16_t adc_buf[ADC_BUF_LEN];

uint16_t adc_data[ADC_DATA_LEN];
uint16_t adc_data2[ADC_DATA_LEN];
uint16_t adc_data3[ADC_DATA_LEN];

int convCount;

// CC variables

float table12[ADC_DATA_LEN*2];
float max12;
int maxindex12;

float table13[ADC_DATA_LEN*2];
float max13;
int maxindex13;

double absoluteDirection;

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_DMA_Init(void);
static void MX_ADC1_Init(void);
static void MX_USART2_UART_Init(void);

/* USER CODE BEGIN PFP */

void crossCorrelation12(uint16_t* data1, uint16_t* data2);
void crossCorrelation13(uint16_t* data1, uint16_t* data3);
double directionAngle(int cc1to2, int cc1to3);
/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */
	convCount = 0 ;
  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_DMA_Init();
  MX_ADC1_Init();
  MX_USART2_UART_Init();
  /* USER CODE BEGIN 2 */
//HAL_Delay(1500);
	HAL_ADC_Start_DMA(&hadc1, (uint32_t*)adc_buf, ADC_BUF_LEN);
	
  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Configure the main internal regulator output voltage
  */
  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSI;
  RCC_OscInitStruct.PLL.PLLM = 16;
  RCC_OscInitStruct.PLL.PLLN = 336;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 7;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV4;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_5) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief ADC1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_ADC1_Init(void)
{

  /* USER CODE BEGIN ADC1_Init 0 */

  /* USER CODE END ADC1_Init 0 */

  ADC_ChannelConfTypeDef sConfig = {0};

  /* USER CODE BEGIN ADC1_Init 1 */

  /* USER CODE END ADC1_Init 1 */
  /** Configure the global features of the ADC (Clock, Resolution, Data Alignment and number of conversion)
  */
  hadc1.Instance = ADC1;
  hadc1.Init.ClockPrescaler = ADC_CLOCK_SYNC_PCLK_DIV4;
  hadc1.Init.Resolution = ADC_RESOLUTION_8B;
  hadc1.Init.ScanConvMode = ENABLE;
  hadc1.Init.ContinuousConvMode = ENABLE;
  hadc1.Init.DiscontinuousConvMode = DISABLE;
  hadc1.Init.ExternalTrigConvEdge = ADC_EXTERNALTRIGCONVEDGE_NONE;
  hadc1.Init.ExternalTrigConv = ADC_SOFTWARE_START;
  hadc1.Init.DataAlign = ADC_DATAALIGN_RIGHT;
  hadc1.Init.NbrOfConversion = 3;
  hadc1.Init.DMAContinuousRequests = ENABLE;
  hadc1.Init.EOCSelection = ADC_EOC_SINGLE_CONV;
  if (HAL_ADC_Init(&hadc1) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure for the selected ADC regular channel its corresponding rank in the sequencer and its sample time.
  */
  sConfig.Channel = ADC_CHANNEL_4;
  sConfig.Rank = 1;
  sConfig.SamplingTime = ADC_SAMPLETIME_3CYCLES;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure for the selected ADC regular channel its corresponding rank in the sequencer and its sample time.
  */
  sConfig.Channel = ADC_CHANNEL_5;
  sConfig.Rank = 2;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure for the selected ADC regular channel its corresponding rank in the sequencer and its sample time.
  */
  sConfig.Channel = ADC_CHANNEL_6;
  sConfig.Rank = 3;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN ADC1_Init 2 */

  /* USER CODE END ADC1_Init 2 */

}

/**
  * @brief USART2 Initialization Function
  * @param None
  * @retval None
  */
static void MX_USART2_UART_Init(void)
{

  /* USER CODE BEGIN USART2_Init 0 */

  /* USER CODE END USART2_Init 0 */

  /* USER CODE BEGIN USART2_Init 1 */

  /* USER CODE END USART2_Init 1 */
  huart2.Instance = USART2;
  huart2.Init.BaudRate = 115200;
  huart2.Init.WordLength = UART_WORDLENGTH_8B;
  huart2.Init.StopBits = UART_STOPBITS_1;
  huart2.Init.Parity = UART_PARITY_NONE;
  huart2.Init.Mode = UART_MODE_TX_RX;
  huart2.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart2.Init.OverSampling = UART_OVERSAMPLING_16;
  if (HAL_UART_Init(&huart2) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN USART2_Init 2 */

  /* USER CODE END USART2_Init 2 */

}

/**
  * Enable DMA controller clock
  */
static void MX_DMA_Init(void)
{

  /* DMA controller clock enable */
  __HAL_RCC_DMA2_CLK_ENABLE();
  __HAL_RCC_DMA1_CLK_ENABLE();

  /* DMA interrupt init */
  /* DMA1_Stream6_IRQn interrupt configuration */
  HAL_NVIC_SetPriority(DMA1_Stream6_IRQn, 0, 0);
  HAL_NVIC_EnableIRQ(DMA1_Stream6_IRQn);
  /* DMA2_Stream0_IRQn interrupt configuration */
  HAL_NVIC_SetPriority(DMA2_Stream0_IRQn, 0, 0);
  HAL_NVIC_EnableIRQ(DMA2_Stream0_IRQn);

}

/**
  * @brief GPIO Initialization Function
  * @param None
  * @retval None
  */
static void MX_GPIO_Init(void)
{

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOH_CLK_ENABLE();
  __HAL_RCC_GPIOA_CLK_ENABLE();

}

/* USER CODE BEGIN 4 */

void HAL_ADC_ConvCpltCallback(ADC_HandleTypeDef* hadc) {
	
	char txt[CHARBUFF_LEN];
	
	int sendBuf = 0;
	
	convCount++ ;
	
	if(hadc == &hadc1)								//check which adc made interrupt
	{
		for(int i = 0 ; i<ADC_BUF_LEN ; ){
			
			adc_data[i/3] = adc_buf[i];	
			i = i + 3 ;
			
		}
		for(int i = 1 ; i<ADC_BUF_LEN ; ){
			
			adc_data2[i/3] = adc_buf[i];	
			i = i + 3 ;
		}
		
		for(int i = 2 ; i<ADC_BUF_LEN ; ){
			
			adc_data3[i/3] = adc_buf[i];	
			i = i + 3 ;
			
		}
	
	}
	for(int i = 0 ; i<ADC_BUF_LEN/3 ; i++ ){ // added
			
			if(adc_data[i] > ADC_THOLD || adc_data2[i] > ADC_THOLD || adc_data3[i] > ADC_THOLD ){	
				sendBuf=1;
				break;
			}
	}
	
	if(sendBuf == 1 ){
		
		crossCorrelation12(adc_data+510,adc_data2+510);
		crossCorrelation13(adc_data+510,adc_data3+510);
		absoluteDirection = directionAngle(maxindex12, maxindex13);
//		for(int i = 0 ; i<4096*3 ;i++ ){
//			
//			sprintf(txt, "%d %d %d\n", adc_data[i]*2, adc_data2[i]*2,adc_data3[i]*2);
//			HAL_UART_Transmit(&huart2, (uint8_t *)txt, strlen(txt),50);
//			
//		}
	
	}
	

}

void crossCorrelation12(uint16_t* data1,uint16_t* data2){

	max12 = 1;
	maxindex12 = -1;
	
	float average112 = 0;
	float average212 = 0;
	
	for (int j = 0; j < ADC_DATA_LEN-510; j++) {
			average112 += data1[j];
	}
	for (int j = 0; j < ADC_DATA_LEN-510; j++) {
			average212 += data2[j];
	}
	
	average112 /= 1.0*ADC_DATA_LEN-510;
	average212 /= 1.0*ADC_DATA_LEN-510;
	int c12=0;

	for (int j = -ADC_DATA_LEN-510; j < ADC_DATA_LEN-510;j++) {
			for (int i = 0; i < ADC_DATA_LEN-510; i++) {
				if ((i+j < ADC_DATA_LEN-510) && (i+j >= 0)) {
					table12[c12] += ((data1[i]-average112) * (data2[i+j]-average212));
				}
			}
			if (table12[c12] > max12) {
				max12 = table12[c12];
				maxindex12 = c12-ADC_DATA_LEN-510;
			}
			c12++;
		}
	
}

void crossCorrelation13(uint16_t* data1, uint16_t* data3){

	max13 = 1;
	maxindex13 = -1;
	
	float average113 = 0;
	float average213 = 0;
	
	for (int j = 0; j < ADC_DATA_LEN-510; j++) {
			average113 += data1[j];
	}
	for (int j = 0; j < ADC_DATA_LEN-510; j++) {
			average213 += data3[j];
	}
	
	average113 /= 1.0*ADC_DATA_LEN-510;
	average213 /= 1.0*ADC_DATA_LEN-510;
	int c13=0;

	for (int j = -ADC_DATA_LEN-510; j < ADC_DATA_LEN-510;j++) {
			for (int i = 0; i < ADC_DATA_LEN-510; i++) {
				if ((i+j < ADC_DATA_LEN-510) && (i+j >= 0)) {
					table13[c13] += ((data1[i]-average113) * (data3[i+j]-average213));
				}
			}
			if (table13[c13] > max13) {
				max13 = table13[c13];
				maxindex13 = c13-ADC_DATA_LEN-510;
			}
			c13++;
		}
	
}

double directionAngle(int cc1to2, int cc1to3){
		
		double micp1 = 1;
		double micp2 = cc1to2;
		double micp3 = cc1to3;
		
		double degreeCalculated = 0;
		
		if(micp1 < micp2 && micp1 < micp3){ // mic1 first arrival
			
			degreeCalculated = 180;
			
			if(micp2 < micp3){ //mic2 second arrival
				
				degreeCalculated -= 60 * ( (micp3-micp2) / micp3 ) ;  
			
			}
			else if(micp3 < micp2){ // mic3 second arrival

				degreeCalculated += 60 * ( (micp2-micp3) / micp2 ) ;
				
			}
		}
		else if(micp2 < micp1 && micp2 < micp3){ // mic2 first arrival
			
			micp1 = micp1 - micp2;
			micp3 = micp3 - micp2;
			degreeCalculated = 60;
			
			if(micp3 < micp1){ //mic3 second arrival
				
				degreeCalculated -= 60 * ( (micp1-micp3) / micp1 ) ;
			
			}
			else if(micp1 < micp3){ // mic1 second arrival
				
				degreeCalculated += 60 * ( (micp3-micp1) / micp3 ) ;
				
			}
			
		}
		
		else if(micp3 < micp1 && micp3 < micp2){
			
			micp1 = micp1 - micp3;
			micp2 = micp2 - micp3;
			degreeCalculated = 300;
			
			if(micp1 < micp2){ //mic3 second arrival
				
				degreeCalculated -= 60 * ( (micp2-micp1) / micp2 ) ;
			
			}
			else if(micp2 < micp1){ // mic1 second arrival
				
				degreeCalculated += 60 * ( (micp1-micp2) / micp1 ) ;
				
			}
			
		}
		// Corner case ifleri yaz
		
		return degreeCalculated;
	}

/* USER CODE END 4 */

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */
  __disable_irq();
  while (1)
  {
  }
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
